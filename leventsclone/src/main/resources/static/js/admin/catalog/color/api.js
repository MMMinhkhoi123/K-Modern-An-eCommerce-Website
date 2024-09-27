function apiOpenFromCreate() {
    axios.get('/admin/color/Open-From-Create', {}).then((e) => {
        $('.admin__screen').html(e.data);
    })
}

function apiOpenFromUpdate(id) {
    axios.get('/admin/color/Open-from-update/' + id, {}).then((e) => {
        $('.admin__screen').html(e.data);
    })
}

function apiCreate(data) {
    axios.post('/admin/color/save', data, {}).then((e) => {
        $('#admin__color-section').html(e.data);
        loaderForm("close");
    })
}

function apiUpdate(data) {
    axios.put('/admin/color/update', data, {}).then((e) => {
        $('#admin__color-section').html(e.data);
        loaderForm("close");
    })
}


function apiDelete(data, type) {
    axios.delete('/admin/color/delete', { data: data } ).then((e) => {
        $('.color-section__list').html(e.data);
        outWaring();
        preCount(data.length);
        loaderForm("close");
    });
}





