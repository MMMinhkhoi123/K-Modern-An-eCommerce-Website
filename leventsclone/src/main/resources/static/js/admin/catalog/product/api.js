function setUp(type) {
    $.get('/admin/product/Data-List/' + type,function (data){
        $("#product--list-fran").html(data);
    })
}

function apiDeleteProduct(data, type) {
    axios.delete("/admin/product/delete",{ data: data }).then((e) => {
        $(".create_List").html(e.data);
        setUpPrice();
        outWaring();
        loaderForm("close");
    });
}

function apiCreate(data) {
    axios.post("/admin/product/save", data, {}).then((e) => {
        $("#product__add--fra").html(e.data);
            setUpEditor(nameJs.describe);
        setUpPrice();
        loaderForm("close");
    });
}

function apiUpdate(data) {
    axios.put("/admin/product/update", data, {}).then((e) => {
        $("#product__add--fra").html(e.data);
            setUpEditor(nameJs.describe);

        setUpPrice();
        loaderForm("close");
    });
}
function apiGetDetailProduct(id) {
    axios.get('/admin/product/detail/' + id, {}).then((e) => {
        $(".admin__screen").html(e.data);
        setUpPriceDetail();
    });
}

function apiGetFromCreate() {
    axios.get('/admin/product/Open-Form-add', {}).then((e) => {
        $(".admin__screen").html(e.data);
        setUpEditor("");
        setUpPrice();
    });
}

function apiGetFromUpdate(id) {
    axios.get("/admin/product/Open-Form-update/" + id, {}).then((e) => {
        $(".admin__screen").html(e.data);
        setUpEditor(nameJs.describe);
        setUpPrice();
    });
}
