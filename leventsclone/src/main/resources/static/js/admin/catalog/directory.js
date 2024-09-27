

function closeFrom() {
    const  classActive = "active";
    const from = document.querySelector(".add-directory");
    from.classList.remove(classActive);
}
function openFrom() {
    axios.get('/fragments-catalog/directory-create', {}).then((e) => {
        $("#admin__directory--from").html(e.data);
    });
}
function update(id) {
    const name = document.querySelector(".add-directory__name > input").value;
    const key = document.querySelector(".add-directory__key > input").value;
    const directory = {
        id: id,
        name: name,
        key: key
    }
    axios.put('/fragments-catalog/directory',directory ,{}).then((e) => {
        $("#admin__directory--from").html(e.data);
        setUp();
    });
}
function getDataUpdate(id) {
    axios.get('/fragments-catalog/directory-update/' + id, {}).then((e) => {
        $("#admin__directory--from").html(e.data);
    });
}

function controlFrom(e, type) {
    e.preventDefault();
 if(type === 'update') {
     const  id = document.querySelector(".add-directory__from").getAttribute("data-id");
     update(id);
 } else  {
     AddDirectory();
 }
}

function deleteDirectory(id){
    axios.delete('/fragments-catalog/directory-delete/' + id, {}).then((e) => {
        $("#admin__directory").html(e.data);
    });
}

