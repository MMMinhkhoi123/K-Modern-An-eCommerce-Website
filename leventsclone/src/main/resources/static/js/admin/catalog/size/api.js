
function apiSetUp() {
    $.get("/fragments-catalog/size", { }, function(data) {
        $("#admin__sizes").html(data);
    });
}


function apiOpenFormCreate() {
    $.get("/admin/size/open-from-create", { }, function(data) {
        $(".admin__screen").html(data);
    });
}


function apiOpenFormUpdate(id) {
    $.get("/admin/size/open-from-update/" + id, { }, function(data) {
        $(".admin__screen").html(data);
    });
}


function apiUpdate(size) {
    axios.put("/admin/size/update", size, {}).then((e) => {
        $("#admin__sector").html(e.data);
        loaderForm("close");
    })
}

function apiCreate(size) {
    axios.post("/admin/size/save", size, {}).then((e) => {
        $("#admin__sector").html(e.data);
        loaderForm("close");
    })
}


function apiDelete(data, type) {
    axios.delete("/admin/size/delete", { data: data }).then((e) => {
        $(".admin__size-List").html(e.data);
        outWaring();
        preCount(data.length);
        loaderForm("close");
    })
}