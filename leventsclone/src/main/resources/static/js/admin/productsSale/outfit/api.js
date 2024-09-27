function apiUpload(file, index) {
    axios.post("/admin/products-api/upload-outfit",file, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleAnimate(index, process);
        }
    })
}

function apiDeleteImg(name) {
    axios.delete("/admin/products-api/delete-outfit/" + name.trim(), {
    }).then((e) => {
    })
}

function apiUpdate(data) {
    axios.put("/admin/outfit/update", data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $(".admin__screen").html(e.data);
        hiddenLoader();
        setUpData();
    })
}

function apiDelete(data) {
    axios.delete("/admin/outfit/delete", {
        data: data
    }).then((e) => {
        $(".screen__List--content").html(e.data);
        hiddenLoader();
        outWaring();
        preCount(data.length);
    })
}

function apiDeleteMix(data, idOutfit) {
    axios.delete("/admin/outfit/delete-mix/" + idOutfit, {
        data: data
    }).then((e) => {
        $(".admin__screen").html(e.data);
        hiddenLoader();
        outWaring();
        // preCount(data.length);
    })
}


function apiCreate(data) {
    axios.post("/admin/outfit/save", data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        hiddenLoader();
        $(".admin__screen").html(e.data);
        setUpData();
    })
}

function apiAddMix(data) {
    axios.post("/admin/outfit/add-mix", data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        hiddenLoader();
        $(".admin__screen").html(e.data);
    })
}
function apiOpenFromUpdate(id) {
    axios.get("/admin/outfit/open-form-update/" + id, {
    }).then((e) => {
        $(".admin__screen").html(e.data);
        setUpData();
    })
}

function apiOpenDetail(id) {
    axios.get("/admin/outfit/open-detail/" + id, {
    }).then((e) => {
        $(".admin__screen").html(e.data);
    })
}

function apiOpenAddMixDetail(id) {
    axios.get("/admin/outfit/open-detail-add-mix/" + id, {
    }).then((e) => {
        $(".admin__screen").html(e.data);
    })
}


function apiGetSize(idOpt) {
    axios.get("/admin/outfit/getSizeProduct/" + idOpt, {
    }).then((e) => {
        $(".form__size-frame").html(e.data);
    })
}

function apiOpenFrom() {
    axios.get("/admin/outfit/open-form-create", {
    }).then((e) => {
        $(".admin__screen").html(e.data);
    })
}