function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}


function loaderForm(type) {
    const loader = document.querySelector(".loader-background");
    if(type === "open") {
        loader.classList.add("active_load_form");
    } else {
        loader.classList.remove("active_load_form");
    }
}

function inputPath(event) {
    const item = event.target;
    item.value = item.value.replaceAll(" ","-");
    item.value = item.value.replaceAll("/","-");
}