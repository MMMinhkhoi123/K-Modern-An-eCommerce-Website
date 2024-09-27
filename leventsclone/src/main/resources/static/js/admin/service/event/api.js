function apiGoAdd() {
    axios.get('/admin/events/form-add', {}).then((e) => {
        $('.admin__screen').html(e.data);
    })
}

function apiDelete(data) {
    axios.delete('/admin/events/delete', { data: data } ).then((e) => {
        $('.admin__screen').html(e.data);
        outWaring();
        preCount(data.length);
        loaderForm("close");
    });
}

function apiGoUpdate(id) {
    axios.get('/admin/events/form-update/' + id, {}).then((e) => {
        $('.admin__screen').html(e.data);
        setUpDate();
    })
}

function apiGoDetail(id) {
    axios.get('/admin/events/form-detail/' + id, {}).then((e) => {
        $('.admin__screen').html(e.data);
    });
}

function apiGoDetailUse(id) {
    axios.get('/admin/events/event-product/' + id + "/use", {}).then((e) => {
        $('.admin__screen').html(e.data);
    });
}

function apiGoDetailNotUse(id) {
    axios.get('/admin/events/event-product/' + id + "/unUse", {}).then((e) => {
        $('.admin__screen').html(e.data);
    });
}

function apiAddEvent(data) {
    axios.post('/admin/events/save',data, {}).then((e) => {
        $('.admin__screen').html(e.data);
        loaderForm("close");
    })
}

function apiUpdateEvent(data) {
    axios.put('/admin/events/update',data, {}).then((e) => {
        $('.admin__screen').html(e.data);
        setUpDate();
        loaderForm("close");
    })
}

function apiAddProductNew(data, id) {
    axios.post("/admin/events/add-product-event/" + id, data, {}).then((e) => {
        $('.admin__screen').html(e.data);
    })
}

function apiDeleteProductNew(data, id) {
    axios.delete("/admin/events/delete-product-event/" + id, {
        data: data
    }).then((e) => {
        $('.admin__screen').html(e.data);
    })
}

function apiUpdateProductEvent(data, id) {
    axios.put("/admin/events/update-product-event/" + id, data , {}).then((e) => {
        $('.admin__screen').html(e.data);
    })
}


