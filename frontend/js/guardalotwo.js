function callRestApi(parameters) {

    $.ajaxSetup({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });

    let options = {}
    options.dataType = 'json'

    if (parameters.method) {
        options.method = parameters.method
    }

    if (parameters.data) {
        options.data = parameters.data
    }

    if (parameters.statusCode) {
        options.statusCode = parameters.statusCode
    }

    if (!parameters.done) {
        parameters.done = function (data, textStatus, jqXHR) {
            console.log(data)
            console.log(textStatus)
            console.log(jqXHR)
        }
    }

    if (!parameters.fail) {
        parameters.fail = function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR)
            console.log(textStatus)
            console.log(errorThrown)
        }
    }

    $.ajax(parameters.url, options)
        .done(parameters.done)
        .fail(parameters.fail);
}