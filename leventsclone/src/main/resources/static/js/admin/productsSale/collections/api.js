function  apiOpenForm() {
    axios.get("/admin/collections/open-from-create").then((e) => {
        $(".admin__screen").html(e.data);
        setArea('');
    })
}

function  apiOpenUpdate(id) {
    axios.get("/admin/collections/open-form-update/" + id).then((e) => {
        $(".admin__screen").html(e.data);
        setArea(dataHIx.describe);
        handleSelectedUpdate();
    })
}

function  apiUpdate(data) {
    axios.put("/admin/collections/update",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $(".admin__screen").html(e.data);
        //set recur areatext;
        setArea(dataHIx.describe);
        hiddenLoader();
    })
}
function  apiCreate(data) {
    axios.post("/admin/collections/save",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $(".admin__screen").html(e.data);
        //set recur areatext;
        setArea(dataHIx.describe);
        hiddenLoader();
    })
}

function  apiDelete(data) {
    axios.delete("/admin/collections/delete",{ data: data}).then((e) => {
        $(".screen__List--content").html(e.data);
        hiddenLoader();
        preCount(data.length);
        outWaring();
    })
}



