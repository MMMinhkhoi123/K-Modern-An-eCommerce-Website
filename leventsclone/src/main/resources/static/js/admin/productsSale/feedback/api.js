function upload(data) {
    axios.post("/admin/feedbacks/save",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    }).then((e) => {
        hiddenLoader();
        $(".admin__screen").html(e.data);
    })
}
function apiUpdate(data) {
    axios.put("/admin/feedbacks/update",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    }).then((e) => {
        hiddenLoader();
        $(".admin__screen").html(e.data);
        setUpUpdate();
    })
}
function apiOpenCreate() {
    axios.get("/admin/feedbacks/open-form-create", {}).then((e) => {
        $(".admin__screen").html(e.data);
    })
}

function apiDelete(data) {
    axios.delete("/admin/feedbacks/delete",{ data: data }).then((e) => {
        $(".screen__List--content").html(e.data);
        outWaring();
        preCount(data.length);
        hiddenLoader();
    });
}
