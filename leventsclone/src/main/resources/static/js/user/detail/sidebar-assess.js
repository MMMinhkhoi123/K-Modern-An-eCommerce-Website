


function  clearSelect(value) {
    document.querySelectorAll(".main__option--item").forEach((e) => {
        if(e !== value) {
            if(e.getAttribute("class").includes("active-star")) {
                e.classList.remove("active-star");
            }
        }
    })
}

const  changeAssess = (e) => {
    const  data = e.target;
    innitPage = data.value;
    goAssessStar(data.value);
}

const  handleSelectStarAssess = (e, type) => {
    clearSelect(e.target);
    e.target.classList.add("active-star");
    innitPage = type.toString();
    goAssessStar(type);
}

function  showAssessStar(type) {
    // const  element = document.querySelector("." + CSS.escape("viewOption__" + type));
    // ClearView(element);
    // element.classList.add("active-view-star");
}

function ClearView(element) {
    document.querySelectorAll(".viewOption__item").forEach((e) => {
        if(e !== element) {
            if(e.getAttribute("class").includes("active-view-star")) {
                e.classList.remove("active-view-star");
            }
        }
    })
}


const redirectLogin = () => {
    setTimeout(() => {
        handleAccountIcon();
    },500)
}


let img = null;

function choiceImgAsses(e) {
    const file = e.target.files[0];
    const item = document.querySelector(".img-upload__assess-item > img");
    const viewImg = document.querySelector(".img-upload__assess");
    const addimg = document.querySelector(".addAssess__form-img--input");
    if(file.type.substring(0, 5) !== "image") {
       alert("Vui lòng chọn hình ảnh");
        img = null;
    } else  {
        img = file;
        viewImg.classList.remove('none-hidden');
        addimg.classList.add("none-hidden");
        var reader  = new FileReader();
        reader.onloadend = function () {
            item.src = reader.result;
        }
        if (file) {
            reader.readAsDataURL(file);
        } else {
            item.src = "";
        }
    }
}




function resetImgAssess() {
    const viewImg = document.querySelector(".img-upload__assess");
    const addimg = document.querySelector(".addAssess__form-img--input");
    viewImg.classList.add('none-hidden');
    addimg.classList.remove("none-hidden");
    img = null;
}

let start_assess = false;

let count_assess = 0;
function choiceAssess(value) {
    count_assess = value + 1;
    const button = document.querySelector(".addAssess__form-button");
    if(button !== undefined) {
        if(start_assess !== true) {
            button.classList.remove("lock");
        }
        start_assess = true;
        const array = document.querySelectorAll(".choice-star__select-item");
        array.forEach((e) => {
            const index = Number(e.getAttribute('data-index'));
            if(index <= value) {
                e.classList.add("active");
            }else  {
                if(e.getAttribute('class').includes('active')) {
                    e.classList.remove("active");
                }
            }
        });



        const title = document.querySelector(".choice-star__select-title");
        switch (value) {
            case 0: {
                title.innerText = "Cần cải thiện thêm";
                break;
            }
            case 1: {
                title.innerText = "Không như kỳ vọng";
                break;
            }
            case 2: {
                title.innerText = "Đúng như kỳ vọng";
                break;
            }
            case 3: {
                title.innerText = "Khá tuyệt vời";
                break;
            }
            case 4: {
                title.innerText = "Tuyệt vời";
                break;
            }
        }
    }
}


function submitAssess(event, thisElement) {
    event.preventDefault();

    const title = document.querySelector(".addAssess__form-title > input");
    const content = document.querySelector(".addAssess__form-content > label > textarea");
    const  order = document.querySelector(".order_selected");
    const formData = new FormData();
    if(img != null) {
        formData.set("file",img);
    }

    formData.set('option', thisElement.getAttribute('data-idOption'));
    formData.set("order", order.value)
    formData.set('title', title.value);
    formData.set('content', content.value);
    formData.set('count', count_assess);
    // lock
    const  lock = document.querySelector(".addAssess__form-button");
    // set load
    const load = document.querySelector(".addAssess__form-button > .form__load");
    load.style.display = "flex";
    lock.classList.add("lock");

    axios.post("/fragments/addAssess", formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $("#viewOption__all").html(e.data);
        optionAdd('close');
        setChoiceAssessImg();
        load.style.display = "none";
        lock.classList.remove("lock");
        cleanFrom(thisElement.getAttribute('data-idOption'))
    })
}

function cleanFrom(option){
    axios.get("/fragments/getOrderRating-clean?option="+ option, {
    }).then((e) => {
        $(".RatingSelect").html(e.data);
    })
}


function scrollInNavigation() {
    const  item = document.querySelector("#viewOption__all--empty-parent");
    item.scrollIntoView();
}

function optionAdd(type) {
    const dataList = document.querySelector(".viewOption__all");
    const data = document.querySelector(".main__viewOption--add");
    const  View = document.getElementById("viewRating");
    const  Add = document.getElementById("viewAddRating");
    if(type === "open") {
        View.classList.add("activeBtn")
        Add.classList.remove("activeBtn")
        data.classList.remove("disable--option");
        dataList.classList.add("disable--option");
    }else  {
        View.classList.remove("activeBtn")
        Add.classList.add("activeBtn")
        data.classList.add("disable--option");
        dataList.classList.remove("disable--option");
    }
}
function changeOrder(event, idOption) {
    var code = event.target.value;
    // get order
    axios.get("/fragments/getOrderRating?code="+ code +"&option=" + idOption, {
    }).then((e) => {
        $(".RatingSelect").html(e.data);
        const textArea = document.querySelector("textarea");
        if(textArea.getAttribute("content") != null) {
            textArea.textContent= textArea.getAttribute("content");
        }
        // start_assess = false;
        // const array = document.querySelectorAll(".choice-star__select-item");
        // array.forEach((e) => {
        //     e.classList.remove("active");
        // });
    })
}

let innitPage = "all";
function goPageAssess(page) {
    const pageSet = (Number(page) - 1);
    const thisElement = document.querySelector(".pagination");
    const form = new FormData();
    form.set('option', thisElement.getAttribute('data-idOption'));
    form.set("page", pageSet);
    form.set("count", innitPage);
    axios.post("/fragments/getPageAssess", form, {
    }).then((e) => {
        $("#viewOption__all").html(e.data);
        setChoiceAssessImg();
        scrollInNavigation();
    })
}


function goAssessStar(count) {
    const thisElement = document.querySelector(".pagination");
    const form = new FormData();
    form.set('option', thisElement.getAttribute('data-idOption'));
    form.set("count", count);

    axios.post("/fragments/getPageAssess-star", form, {
    }).then((e) => {
        $("#viewOption__all").html(e.data);
        setChoiceAssessImg();
    });
}



function setChoiceAssessImg() {
    const  item = document.querySelector(".main__link-filter > input").checked;
    if(item) {
        const array = document.querySelectorAll(".viewOption__all--item");
        array.forEach((e) => {
            const x = e.querySelector(".assess__img");
            if(x == null) {
                e.classList.add("disable-item-img");
            }
        })
    } else  {
        const array = document.querySelectorAll(".viewOption__all--item");
        array.forEach((e) => {
            const x = e.querySelector(".assess__img");
            if(e.getAttribute('class').includes("disable-item-img")) {
                e.classList.remove("disable-item-img");
            }
        })
    }
}




































