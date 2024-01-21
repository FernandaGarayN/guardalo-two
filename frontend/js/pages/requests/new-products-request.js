const requestedProducts = [];

function updateRequestedProducts() {
    $("#carritoTabla tbody").empty();
    requestedProducts.forEach(function (producto, index) {
        $("#carritoTabla tbody").append(
            '<tr>' +
            '<td>' + producto.productSku + '</td>' +
            '<td>' + producto.quantity + '</td>' +
            '<td><button onclick="removeRequestedProduct(' + index + ')" class="btn btn-danger btn-sm">Eliminar</button></td>' +
            '</tr>'
        );
    });
}

function removeRequestedProduct(index) {
    requestedProducts.splice(index, 1);
    updateRequestedProducts();
};

$(document).ready(function () {
    $("#subsidiary").prop('disabled', true);
    $("#address").prop('disabled', true);
    $("#destination").prop('disabled', true);
    $("#sendRequestedProducts").prop('disabled', true);
    $("#productSku").prop('disabled', true);
    $("#quantity").prop('disabled', true);
    $("#addProductToCart").prop('disabled', true);

    $("#warehouseCompanySelect").change(function () {
        let warehouseCompany = $(this).val();

        $("#warehouseCompany").val(warehouseCompany)

        // Si la empresa de transporte es "Guárdalo Two", muestra los campos y borra los valores por defecto
        if (warehouseCompany === "guardalotwo") {
            $("#subsidiary").prop('disabled', false);
            $("#address").prop('disabled', false);
            $("#subsidiary").val("");
            $("#address").val("");
        } else {
            // Si no, oculta los campos y establece los valores por defecto
            $("#subsidiary").prop('disabled', true);
            $("#address").prop('disabled', true);
            $("#subsidiary").val("Bodega Guardalo Two");
            $("#address").val("Calle Patrañas #2310");
        }

        // Si la empresa de transporte es la deseada, habilita los campos
        if (warehouseCompany !== "empty") {
            $("#destination").prop('disabled', false);
            $("#sendRequestedProducts").prop('disabled', false);
            $("#productSku").prop('disabled', false);
            $("#quantity").prop('disabled', false);
            $("#addProductToCart").prop('disabled', false);
        } else {
            // Si no, deshabilita los campos
            $("#destination").prop('disabled', true);
            $("#sendRequestedProducts").prop('disabled', true);
            $("#productSku").prop('disabled', true);
            $("#quantity").prop('disabled', true);
            $("#addProductToCart").prop('disabled', true);
        }
        // Borrar el carrito
        requestedProducts.length = 0;
        updateRequestedProducts();

        $("#productSku").autocomplete({
            search: function() {
                $("#spinner-auto").show();
            },
            response: function() {
                $("#spinner-auto").hide();
            },
            source: function (request, response) {
                $.ajax({
                    url: `http://localhost:8081/api/v1/warehouses/products/search?transportEnterprise=${warehouseCompany}`,
                    type: 'GET',
                    data: {
                        term: request.term
                    },
                    success: function (data) {
                        console.log("data=", data)
                        const filteredData = data.filter(function (item) {
                            return !requestedProducts.find(function (producto) {
                                return producto.productSku === item.sku
                            });
                        });

                        console.log("filteredData=", filteredData)

                        const modifiedData = filteredData.map(function (item) {
                            return {
                                value: item.display,
                                sku: item.sku,
                                price: item.price,
                                stock: item.stock,
                                id: item.id
                            };
                        });
                        response(modifiedData);
                    },
                    error: function (error) {
                        console.log(error);
                    }
                });
            },
            minLength: 2,
            select: function (event, ui) {
                $("#productSku").val(ui.item.sku)
                $("#productSku").attr('data-id', ui.item.id)
                $("#productDisplay").val(ui.item.value)
                $("#productPrice").val(ui.item.price)
                $("#quantity").attr('data-max', ui.item.stock)
                $("#maxStock").text("Stock máximo: " + ui.item.stock);
                return false;
            }
        });
    });

    $("#sendRequestedProducts").click(function () {
        const subsidiary = $("#subsidiary").val();
        const address = $("#address").val();
        const warehouseCompany = $("#warehouseCompany").val();
        if (requestedProducts.length > 0 && subsidiary !== "" && address !== "") {
            const data = {
                warehouse: warehouseCompany,
                subsidiary: subsidiary,
                address: address,
                details: requestedProducts.map(function (item) {
                    return {
                        id: item.id,
                        sku: item.productSku,
                        name: item.name,
                        quantity: item.quantity,
                        price: item.price,
                        description: item.description
                    };
                }),
            };


            $.ajaxSetup({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });

            console.log("Enviando solicitud de productos:", data);

            let parameters = {}
            parameters.url = "http://localhost:8081/api/v1/warehouses/requests"
            parameters.method = "POST"
            parameters.data = JSON.stringify(data)
            parameters.done = function (data, textStatus, jqXHR) {
                console.log(`POST requests response status: ${textStatus}`)
                $('#successMessage p').text(`Solicitud ingresada exitosamente!`);
                $('#successMessage').fadeIn(500).delay(3000).fadeOut(500);

                setTimeout(function () {
                    window.location.href = "list.html"
                }, 4500)
            }
            parameters.fail = function (jqXHR, textStatus, errorThrown) {
                console.log(`POST requests response status: ${textStatus}`)
                console.log(`POST requests error: ${errorThrown}`)
                $('#dangerMessage p').text('Ha ocurrido un error al ingresar la solicitud!');
                $('#dangerMessage').fadeIn(500).delay(3000).fadeOut(500);
            }

            callRestApi(parameters);

        } else {
            $('#dangerMessage p').text('Por favor, agrega productos y especifica una sucursal y su dirección.');
            $('#dangerMessage').fadeIn(500).delay(3000).fadeOut(500);
        }
    });

    $("#addProductToCart").click(function () {
        let id = $("#productSku").attr('data-id');
        let sku = $("#productSku").val();
        let cantidad = $("#quantity").val();
        let maxStock = $("#quantity").attr('data-max');
        let productDisplay = $("#productDisplay").val();
        let productPrice = $("#productPrice").val();

        // Validación de campos no vacíos
        if (sku === '' || cantidad === '' || parseInt(cantidad, 10) <= 0) {
            $('#dangerMessage p').text('Por favor, rellena todos los campos y asegúrate de que la cantidad sea mayor que cero.');
            $('#dangerMessage').fadeIn(500).delay(3000).fadeOut(500);
            return;
        } else if (parseInt(cantidad, 10) > parseInt(maxStock, 10)) {
            $('#dangerMessage p').text('La cantidad ingresada excede el stock disponible.');
            $('#dangerMessage').fadeIn(500).delay(3000).fadeOut(500);
            return;
        }

        for (let i = 0; i < requestedProducts.length; i++) {
            if (requestedProducts[i].productSku === sku) {
                $('#dangerMessage p').text('Este producto ya está solicitado.');
                $('#dangerMessage').fadeIn(500).delay(3000).fadeOut(500);
                return;
            }
        }

        let producto = {
            id: parseInt(id, 10),
            productSku: sku,
            name: productDisplay,
            description: productDisplay,
            price: productPrice,
            quantity: parseInt(cantidad, 10)
        };

        console.log("Producto a agregar:", producto)

        requestedProducts.push(producto);
        updateRequestedProducts();

        $("#productSku").val('');
        $("#productSku").attr('data-id', null);
        $("#quantity").val('');
        $("#quantity").attr('data-max', null);
        $("#maxStock").text('');
    });
});

$(document).ajaxStart(function() {
    $("#spinner-send").show();
});

$(document).ajaxStop(function() {
    $("#spinner-send").hide();
});