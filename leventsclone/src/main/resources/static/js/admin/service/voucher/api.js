function apiOpenCreate() {
    axios.get("/admin/vouchers/open-from-create", {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
    })
}

function apiOpenUpdate(id) {
    axios.get("/admin/vouchers/open-from-update/" + id, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
    })
}

function apiCreateVoucher(data) {
    axios.post("/admin/vouchers/save",data, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
        loaderForm("close");
    })
}
function apiUpdateVoucher(data) {
    axios.put("/admin/vouchers/update",data, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
        loaderForm("close");
    })
}

function apiDeleteVoucher(data) {
    axios.delete("/admin/vouchers/delete", { data: data }).then(e => {
        $(".screen__List--content").html(e.data);
        setUpPrice();
        preCount(data.length);
        outWaring();
        loaderForm("close");
    })
}