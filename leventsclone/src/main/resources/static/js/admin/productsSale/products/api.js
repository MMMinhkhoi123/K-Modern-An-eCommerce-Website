function apiOpenForm() {
    axios.get("/admin/products/open-from-add").then((e) => {
        $(".admin__screen").html(e.data);
    })
}

function apiOpenFormSize(id) {
    axios.get("/admin/products/open-from-add-size/" + id).then((e) => {
        $(".admin__screen").html(e.data);
    })
}


function apiOpenFormUpdate(id) {
    axios.get("/admin/products/open-from-update/" + id ).then((e) => {
        $(".admin__screen").html(e.data);
        handleSelectedUpdate();
    })
}

function apiOpenDetail(value) {
    axios.get("/admin/products/detail/" + value ).then((e) => {
        $(".admin__screen").html(e.data);
    })
}

function apiUpload(file, index) {
    axios.post("/admin/products-api/upload",file, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleAnimate(index, process);
        }
    }).then((e) => {
    })
}

function apiAddSize(data) {
    axios.post("/admin/products/addSize-save",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $(".admin__screen").html(e.data);
    })

}

function apiUploadRefund(file, index) {
    axios.post("/admin/products-api/upload",file, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleAnimate(index, process);
        }
    }).then((e) => {
    })
}

function apiDeleteImg(name) {
    axios.delete("/admin/products-api/delete/" + name.trim(), {
    }).then((e) => {
    })
}

function apiDelete(Data) {
    axios.delete("/admin/products/delete", {
        data: Data
    }).then((e) => {
        $('.admin__screen').html(e.data);
        hiddenLoader();
        preCount(Data.length);
    })
}

function apiDeleteSize(Data, idOpt) {
    axios.delete("/admin/products/delete-option-size/" + idOpt, {
        data: Data
    }).then((e) => {
        $('.admin__screen').html(e.data);
        hiddenLoader();
        preCount(Data.length);
    })
}


function apiCreateProducts(formData) {
    axios.post("/admin/products/save",formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $('#admin__screen--section').html(e.data);
        hiddenLoader();
    })
}

function apiUpdateProducts(formData) {
    axios.put("/admin/products/update",formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $('#admin__screen--section').html(e.data);
        hiddenLoader();
        handleSelectedUpdate();
    })
}


function apiGetColors(id) {
    axios.get("/admin/products/color-by-product/" + id, {}).then((e) => {
      $(".form--color-parent").html(e.data);
    })
}