function apiOpenCreate() {
    axios.get("/admin/members/open-from-create", {}).then(e => {
        $(".admin__screen").html(e.data);
    })
}
function apiOpenUpdate(id) {
    axios.get("/admin/members/open-from-update/" + id, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
    })
}


function apiCreateVoucher(data) {
    axios.post("/admin/members/save",data, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
        loaderForm("close");
    })
}

function apiUpdateVoucher(data) {
    axios.put("/admin/members/update",data, {}).then(e => {
        $(".admin__screen").html(e.data);
        setUpPrice();
        loaderForm("close");
    })
}

function apiDeleteVoucher(data) {
    axios.delete("/admin/members/delete", { data: data }).then(e => {
        $(".screen__List--content").html(e.data);
        setUpPrice();
        preCount(data.length);
        outWaring();
        loaderForm("close");
    })
}