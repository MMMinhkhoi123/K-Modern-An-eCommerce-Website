function apiOpenFrom() {
    axios.get('/admin/directory/open-from-create', {}).then((e) => {
        $(".admin__screen").html(e.data);
    });
}


function apiGetFromUpdate(id) {
    axios.get('/admin/directory/open-from-update/' + id, {}).then((e) => {
        $(".admin__screen").html(e.data);
    });
}

function apiCreate(directory) {
    axios.post('/admin/directory/save',directory , {}).then((e) => {
        $(".admin__screen").html(e.data);
        loaderForm("close");
    });
}

function apiUpdate(directory) {
    axios.put('/admin/directory/update',directory ,{}).then((e) => {
        $(".admin__screen").html(e.data);
        loaderForm("close");
    });
}


function apiDelete(data, type) {
    axios.delete('/admin/directory/delete', { data: data }).then((e) => {
        $(".parent__directory--list").html(e.data);
        preCount(data.length);
        loaderForm("open");
        outWaring();
    });
}
