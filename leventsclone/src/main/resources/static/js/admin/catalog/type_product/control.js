const  url = window.location.origin + window.location.pathname;

function OpenForm() {
    window.history.replaceState(null,null, url + "?action=add");
    apiOpenForm();
}

function OpenFromUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiOpenFromUpdate(id);
}



function setUpData(
    id, name, url, directory,
) {
    return {
        id: id,
        name: name,
        key: url,
        idDirectory: directory,
    };
}

function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function controlFrom(e, type) {
    e.preventDefault();
    loaderForm("open");
    const name = document.querySelector(".add-type-product__name > input").value;
    const url = document.querySelector(".add-type-product__url > input").value;
    const directory = document.querySelector(".add-type-product__directory > select").value;
    if(type === "add") {
        const  data = setUpData(null, name, url, directory);
        apiCreate(data);
    }else  {
        const  id = document.querySelector(".add-type-product")
            .getAttribute("data-id");
        const  data = setUpData(id, name, url, directory);
        apiUpdate(data);
    }
}


function confirmDelete() {
    loaderForm("open");
    deleteSize(this.type);
    clearAll();
}


function deleteSize(type) {
    const  array = document.querySelectorAll(".table__check > input");
    let object = [];
    array.forEach((e) => {
        if(e.checked === true) {
            object.push(e.getAttribute("data-id"));
        }
    })

        apiDelete(object, type);
}