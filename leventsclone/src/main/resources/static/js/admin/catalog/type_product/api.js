function apiSetUp() {
    $.get('/fragments-catalog/type-product', function (data) {
        $("#admin__type-product").html(data);
    })
}

function apiDelete(data, type) {
    axios.delete('/admin/type_product/delete/' + type, { data: data }).then((e) => {
        $(".parent__type-product").html(e.data);
        outWaring();
        preCount(data.length);
        loaderForm("close");
    })
}

function apiOpenForm() {
    $.get('/admin/type_product/open-form-create', function (data) {
        $("#admin__screen").html(data);
    })
}

function apiOpenFromUpdate(id) {
    $.get('/admin/type_product/open-form-update/' + id, function (data) {
        $("#admin__screen").html(data);
    })
}

function apiUpdate(data) {
    axios.put('/admin/type_product/update', data, {}).then((e) => {
        $("#type__farm").html(e.data);
        loaderForm("close");
    })
}


function apiCreate(data) {
    axios.post('/admin/type_product/save', data, {}).then((e) => {
        $("#type__farm").html(e.data);
        loaderForm("close");
    })
}