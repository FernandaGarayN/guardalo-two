const carrito = [];

function actualizarCarrito() {
    $("#carritoTabla tbody").empty();
    carrito.forEach(function (producto, index) {
        $("#carritoTabla tbody").append(
            '<tr>' +
            '<td>' + producto.productSku + '</td>' +
            '<td><input type="number" value="' + producto.quantity + '" onchange="cambiarCantidad(' + index + ', this.value)"></td>' +
            '<td><button onclick="eliminarProducto(' + index + ')" class="btn btn-danger btn-sm">Eliminar</button></td>' +
            '</tr>'
        );
    });
}

function cambiarCantidad(index, nuevaCantidad) {
    if (nuevaCantidad <= 0) {
        eliminarProducto(index);
    } else {
        carrito[index].quantity = nuevaCantidad;
    }
    actualizarCarrito();
};

function eliminarProducto(index) {
    carrito.splice(index, 1);
    actualizarCarrito();
};

$(document).ready(function () {

    $("#productSku").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "http://localhost:8081/products/search",
                type: 'GET',
                data: {
                    term: request.term
                },
                success: function (data) {
                    const filteredData = data.filter(function (item) {
                        return !carrito.find(function (producto) {
                            return producto.productSku === item.sku
                        });
                    });

                    const modifiedData = filteredData.map(function (item) {
                        return {
                            value: item.display,
                            sku: item.sku,
                            stock: item.stock
                        };
                    });
                    response(modifiedData);
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            $("#productSku").val(ui.item.sku)
            $("#quantity").attr('data-max', ui.item.stock)
            $("#maxStock").text("Stock máximo: " + ui.item.stock);
            return false;
        }
    });

    $("#addProductToCart").click(function () {
        let sku = $("#productSku").val();
        let cantidad = $("#quantity").val();
        let maxStock = $("#quantity").attr('data-max');

        // Validación de campos no vacíos
        if (sku === '' || cantidad === '' || parseInt(cantidad, 10) <= 0) {
            alert("Por favor, rellena todos los campos y asegúrate de que la cantidad sea mayor que cero.");
            return;
        } else if (parseInt(cantidad, 10) > parseInt(maxStock, 10)) {
            alert("La cantidad ingresada excede el stock disponible.");
            return;
        }

        for (let i = 0; i < carrito.length; i++) {
            if (carrito[i].productSku === sku) {
                alert("Este producto ya está en el carrito.");
                return;
            }
        }

        let producto = {
            productSku: sku,
            quantity: parseInt(cantidad, 10)
        };

        carrito.push(producto);
        actualizarCarrito();

        $("#productSku").val('');
        $("#quantity").val('');
        $("#maxStock").text('');
    });

    // Función para enviar todo el carrito
    $("#enviarCarrito").click(function () {
        const destino = $("#destination").val();
        if (carrito.length > 0 && destino !== "") {
            const datosCarrito = {
                productos: carrito,
                destino: destino
            };


            $.ajaxSetup({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });

            console.log("Enviar carrito:", datosCarrito);
            // Aquí debes agregar el código para enviar el carrito al servidor
            $.ajax({
                url: 'URL_DEL_SERVIDOR_API',  // Reemplaza con la URL de tu API
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(datosCarrito),
                success: function (response) {
                    console.log("Respuesta del servidor:", response);
                    // Aquí puedes añadir código para manejar una respuesta exitosa
                },
                error: function (xhr, status, error) {
                    console.error("Error al enviar datos:", error);
                    // Aquí puedes añadir código para manejar errores
                }
            });
        } else {
            alert("Por favor, agrega productos al carrito y especifica un destino.");
        }
    });
});