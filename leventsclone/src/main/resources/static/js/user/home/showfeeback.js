let active_feedback = 0;
let default_feedback;
let next_feedback;
let listParentFeedback = document.querySelectorAll( ".show-feedback-parent");
let manageAction = false;
const runFeedback = () => {
    let keyDefault = "defaultFeedBack";
    let keyActive = "activeFeedBack";
    let keyNext = "nextFeedBack";

    default_feedback = active_feedback - 1;
    next_feedback = active_feedback  + 1;
    if(default_feedback < 0) {
        default_feedback = (listParentFeedback.length - 1);
    }
    if(next_feedback > (listParentFeedback.length - 1) ) {
        next_feedback = next_feedback - (listParentFeedback.length - 1) -1;
    }
    const active =  document.querySelector("."  + CSS.escape( "feedback" + active_feedback ) );
    active.classList.add(keyActive);
    active.classList.remove(keyNext)
    active.classList.remove(keyDefault)
    const defaults =  document.querySelector("." +  CSS.escape('feedback' + default_feedback));
    console.log(defaults);
    defaults.classList.add(keyDefault);
    defaults.classList.remove(keyActive);
    defaults.classList.remove(keyNext);
    const next =  document.querySelector("." +  CSS.escape("feedback"  + next_feedback));

    console.log(next);
    next.classList.add(keyNext);
    next.classList.remove(keyDefault);
    next.classList.remove(keyActive);

    active.style.opacity = 1;
    next.style.opacity = 0;
    defaults.style.opacity = 0;
}
runFeedback();




function nextFeedback(event) {
    console.log("minhkhoi")
    event.preventDefault();
    active_feedback = active_feedback + 1;
    if(active_feedback > listParentFeedback.length - 1) {
        active_feedback = 0;
    }
    runFeedback();
}

function preFeedback(event) {
    event.preventDefault();
    active_feedback = active_feedback - 1;
    if(active_feedback < 0) {
        active_feedback = listParentFeedback.length - 1;
    }
        runFeedback();
}
function handleMouseDownFeedback(e, thisElement) {
    e.preventDefault();
    const detailX = e.clientX;
    let positionEnd = 0;
    function handleMouseMove(event) {
        manageAction = false;
        const  position = event.clientX - detailX;
        handleHide(position, thisElement);
        handleVisible(position, thisElement);
        positionEnd = position;
    }
    function handleMouseUp() {
        thisElement.removeEventListener("mousemove", handleMouseMove);
        thisElement.removeEventListener("mouseup", handleMouseUp);
        if(manageAction === false) {
            handPositionThisFeed(thisElement,positionEnd);
        }
    }
    // function handleMouseLeave() {
    //     handleMouseUp();
    // }
    thisElement.addEventListener('mousemove', handleMouseMove);
    thisElement.addEventListener('mouseup', handleMouseUp);
    // thisElement.addEventListener('mouseleave', handleMouseLeave)
}




function  handleVisible(position, thisElement) {
    const  item = getElementNext(position, thisElement);
    if(position < 0) {
        position = position * (-1);
    }
    item.style.opacity = position / 500 ;
}

function getElementNext(position, thisElement) {
    const  index  = thisElement.getAttribute("data-index");
    let  data = null;
    if(position > 0) {
        let  indexUse = Number(index) + 1;
        if(indexUse > listParentFeedback.length -1) {
            indexUse = 0;
        }
        data = document.querySelector("." + CSS.escape('feedback' + indexUse));
    }else  {
        let  indexUse = Number(index) - 1;
        if(indexUse < 0) {
            indexUse = listParentFeedback.length - 1;
        }
        data = document.querySelector("." + CSS.escape('feedback' + indexUse));
    }
    return data;
}




function  handleHide(position, thisElement) {
    if(position < 0) {
        position = position * (-1);
    }
    thisElement.style.opacity = 1 - position / 500;
}


function handPositionThisFeed(thisElement,position) {
    const  item = getElementNext(position, thisElement);
    let widthSet = 400;
    function myFunction(x) {
        if (x.matches) { // If media query matches
            widthSet = 200;
        }
    }
    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);

    if(position < widthSet) {
        if(position < -widthSet) {
            preFeedback(event);
        } else  {
            thisElement.style.opacity = 1;
            item.style.opacity = 0;
        }
    } else  {
        nextFeedback(event);
    }
}


function  setUpLink() {
    listParentFeedback.forEach((item) => {
        const  dataPress = item.querySelector(".right__button > a");
        const color = ConvertUrl(item.getAttribute("data-color"));
        const  name =  ConvertUrl(item.getAttribute("data-name"));
        const  namFull = name + "-" + color;
        dataPress.setAttribute("href", "/products/" +namFull );
    })
}
setUpLink();
function ConvertUrl(name) {
    const nameLV1= name.replaceAll(" ","-");
    return nameLV1.replaceAll("/","-");
}
function disableScroll(e) {
    e.preventDefault();
}

function  openShowFeedback(e,index) {
    e.stopPropagation();
    const container = document.querySelector(".container")
    var root = document.documentElement;
    if(manageMouseUpSwiper === true) {
        const feedBackShow = document.querySelector(".show-feedback");
        active_feedback = Number(index);
        runFeedback();
        feedBackShow.classList.add("active__feedback");
        root.style.setProperty('--heightScroll', '0px');
    }
}

function  openShowFeedbackV2(e,index) {
    e.stopPropagation();
    var root = document.documentElement;
    root.style.setProperty('--heightScroll', '0px');
    const feedBackShow = document.querySelector(".show-feedback");
    active_feedback = Number(index);
    runFeedback();
    feedBackShow.classList.add("active__feedback");
}


function  offShowFeedback() {
    const feedBackShow = document.querySelector(".show-feedback");
    feedBackShow.classList.remove("active__feedback")
    var root = document.documentElement;
    root.style.setProperty('--heightScroll', '5px');
}




















const feedBackSets = document.querySelectorAll(".show-feedback-parent");

feedBackSets.forEach((element) => {

    element.addEventListener('touchstart', (event) => {


        const detailX = event.touches[0].clientX;
        let positionEnd = 0;

        const handleMove = (event2) => {
            manageAction = false;
            event2.preventDefault();
            const position = event2.touches[0].clientX - detailX;
            handleHide(position, element);
            handleVisible(position, element);
            positionEnd = position;
        }
        const  handleUp = () => {
            element.removeEventListener("touchmove",handleMove);
            element.removeEventListener("touchend",handleUp);
            if(manageAction === false) {
                handPositionThisFeed(element,positionEnd);
            }
        }
        element.addEventListener('touchmove', handleMove);
        element.addEventListener('touchend',handleUp);

    });
})