let thumbScroll =null;
let  parentThumbScroll = null;
let  list = null;
let  itemList = null;
let itemOne = null;
let itineraryThumb = null;
let managerMouseMoveSwiper = true;
let manageMouseUpSwiper = true;
let widthThumbSwiperFact = null;

let percentPassItem = 20;
const smoothPass = "0.2s";

function getPercentSwiper() {
    return itemOne.offsetWidth / widthThumbSwiperFact;
}

const getInitPositionCollection = () => {
   return thumbScroll.offsetLeft * getPercentSwiper();
}

// const getaPotionCollect = (positionCurrent, initialPosition) => {
// return -initialPosition + (positionCurrent * getPercentSwiper());
// }
const getaPotionCollect = (positionCurrent, initialPosition) => {
    return -initialPosition + positionCurrent;
}

const getaPotionThumb = (positionCurrent, initialPosition) => {
   return initialPosition +  -(positionCurrent / getPercentSwiper());
}

let handleMouseMouse = null;
let handleMouseUp = null;
let savePosition = 0;
let itemPre = null;
let itemNext = null;
function handleMouseDownSwiper(e, thisElement) {
e.preventDefault();

 itemPre = document.querySelector("." + CSS.escape(thisElement.getAttribute('data-key') + "__pre"));
 itemNext = document.querySelector("." + CSS.escape(thisElement.getAttribute('data-key') + "__next"));

managerMouseMoveSwiper = true;
const detailX = e.clientX;
const detailY = e.clientY;
 savePosition = 0;
const initialPositionThumb = thumbScroll.offsetLeft;
const initialPositionCollection = getInitPositionCollection();

handleMouseMouse = (e)=> {
    manageMouseUpSwiper = false;
    let positionY = e.clientY - detailY;
    if(positionY < 0) {
        positionY = -1 * positionY;
    }
    if(managerMouseMoveSwiper === true && positionY < 30) {
        const position = e.clientX - detailX;
        thumbScroll.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
        thisElement.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
        savePosition = position;
    }
}


handleMouseUp = () => {
    thisElement.removeEventListener('mousemove', handleMouseMouse);
    thisElement.removeEventListener('mouseup', handleMouseUp);

    handlePositionMouseUpCollect(thisElement, thumbScroll);
    handlePositionMouseUpThumb(thumbScroll);
    if(savePosition > 0) {
        percentPassItem = 70;
    }else  {
        percentPassItem = 30;
    }
    thumbScroll.style.transitionDuration = '0s';
    thisElement.style.transitionDuration = '0s';

    if(manageMouseUpSwiper === false) {
        thisElement.addEventListener('click', handleClick)
    }

}

function  handleClick(e) {
    e.preventDefault();
    thisElement.removeEventListener("click", handleClick)
}


thisElement.addEventListener('mousemove', handleMouseMouse);
thisElement.addEventListener('mouseup', handleMouseUp);
}

function handleMouseLeaveSwiper(thisElement) {
managerMouseMoveSwiper = false;
    thisElement.removeEventListener('mousemove', handleMouseMouse);
    thisElement.removeEventListener('mouseup', handleMouseUp);
    handlePositionMouseUpCollect(thisElement, thumbScroll);
    handlePositionMouseUpThumb(thumbScroll);
    if(savePosition > 0) {
        percentPassItem = 70;
    }else  {
        percentPassItem = 30;
    }
    thumbScroll.style.transitionDuration = '0s';
    thisElement.style.transitionDuration = '0s';
}


const handlePositionMouseUpCollect = (thisElement, thumb) => {
if(thumbScroll.offsetLeft < 0) {
    thisElement.style.transform = "translate3d(0, 0 , 0)";
    if(itemPre != null) {
        itemPre.style.display = "none";
        itemNext.style.display = "block";
    }
} else
    if(itemPre != null) {
        itemPre.style.display = "block";
        itemNext.style.display = "block";
    }
if(thumbScroll.offsetLeft > itineraryThumb) {
    const width = itineraryThumb * getPercentSwiper()
    thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    if(itemNext != null) {
        itemNext.style.display = "none";
    }
} else  {
    const positionCurrent =  thumbScroll.offsetLeft * getPercentSwiper();

    const extra = positionCurrent  % itemOne.offsetWidth;
    const percent = extra * 100 / itemOne.offsetWidth;
    if(percent > percentPassItem) {
        const  data = ((100 - percent) / 100 ) * itemOne.offsetWidth;
        const  width = positionCurrent + data;
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    } else  {
        const widthMinus = (percent / 100) * itemOne.offsetWidth;
        const width = (thumbScroll.offsetLeft * getPercentSwiper()) - widthMinus;
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    }
}

thisElement.style.transitionDuration = smoothPass;
}

const handlePositionMouseUpThumb = (thumbElement) => {
if(thumbElement.offsetLeft < 0) {
    thumbElement.style.left = 0 + 'px';
}else
if(thumbElement.offsetLeft > itineraryThumb) {
    thumbElement.style.left = itineraryThumb + 'px';
} else  {
    const extra = thumbElement.offsetLeft % widthThumbSwiperFact;
    const  percent = extra * 100 / widthThumbSwiperFact;
    if(percent > percentPassItem) {
        const  width = ((100 - percent) / 100) * widthThumbSwiperFact;
        const widthUse = width + thumbElement.offsetLeft;
        thumbElement.style.left = widthUse + 'px';
        thumbElement.style.transitionDuration = '0.4s';
        if(widthUse === itineraryThumb) {
            if(itemNext != null) {
                itemNext.style.display = "none";
            }
        }
    } else  {
        const widthMinus = (percent / 100) * widthThumbSwiperFact;
        const widthUse = thumbElement.offsetLeft - widthMinus;
        thumbElement.style.left =  widthUse + 'px';
        thumbElement.style.transitionDuration = '0.4s';
        if(widthUse === 0) {
            if(itemPre != null) {
                itemPre.style.display = "none";
            }
        }
    }
}

thumbElement.style.transitionDuration = '0.4s';
}


function handlePreSwiper() {
    const position = thumbScroll.offsetLeft - widthThumbSwiperFact ;

    const widthThumb = thumbScroll.offsetLeft - widthThumbSwiperFact;
    const  widthCollection = (thumbScroll.offsetLeft * getPercentSwiper())  - itemOne.offsetWidth;
    if(position > 0) {
        thumbScroll.style.left = widthThumb  + "px";
        list.style.transform = "translate3d(-"+ widthCollection +"px, 0 ,0)";
    }else  {
        thumbScroll.style.left = 0  + "px";
        list.style.transform = "translate3d("+ 0 +"px, 0 ,0)";
    }
    list.style.transition = "1s";
}
function  handleNextSwiper() {
    const position = thumbScroll.offsetLeft + widthThumbSwiperFact ;

    const widthThumb = thumbScroll.offsetLeft + widthThumbSwiperFact;
    const  widthCollection = (thumbScroll.offsetLeft * getPercentSwiper())  + itemOne.offsetWidth;

    if(position <  itineraryThumb) {
        thumbScroll.style.left = widthThumb  + "px";
        list.style.transform = "translate3d(-"+ widthCollection +"px, 0 ,0)";
    } else  {
        thumbScroll.style.left = itineraryThumb + "px";
        list.style.transform = "translate3d(-"+ itineraryThumb * getPercentSwiper() +"px, 0 ,0)";
    }
    list.style.transition = "1s";
}


function resetSwiper() {
thumbScroll.style.left = 0;
list.style.transform = "translate3d(0, 0 , 0)";
}












let managerMobileClick = true;

document.querySelectorAll(".swiper__wrapper").forEach((e) => {
    const id = e.getAttribute('id');
    const item = document.querySelector("#" + CSS.escape(id));

    itemPre = document.querySelector("." + CSS.escape(e.getAttribute('data-key') + "__pre"));
    itemNext = document.querySelector("." + CSS.escape(e.getAttribute('data-key') + "__next"));

    const func = () => {
        manageMouseUpSwiper = true;
        if(id === "product-arrive") {
            SetUp('.scrollbar--move','.swiper__scroll--frame','.product-arrive', '.item--arrive');
        }else  {
            SetUp('.scrollbar--move','.swiper__scroll--frame','.product-seller', '.item--seller')
        }
        const detailX = event.touches[0].clientX;
        const detailY = event.touches[0].clientY;
        const initialPositionThumb = thumbScroll.offsetLeft;
        const initialPositionCollection = getInitPositionCollection();
        const handleMove = (event2) => {
            managerMobileClick = false;
            const position = event2.touches[0].clientX - detailX;
            let positionY = event2.touches[0].clientY - detailY;
            if(positionY < 0) {
                positionY = positionY * -1;
            }
            if(positionY > 20) {
                item.removeEventListener("touchmove", handleMove);
            }else  {
                event2.preventDefault();
                thumbScroll.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
                item.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
                savePosition = position;
            }
        }
        const  handleUp = () => {
            item.removeEventListener("touchmove",handleMove);
            item.removeEventListener("touchend",handleUp);

            if(savePosition > 0) {
                percentPassItem = 85;
            } else  {
                percentPassItem = 15;
            }
            handlePositionMouseUpCollect(item, thumbScroll);
            handlePositionMouseUpThumb(thumbScroll);
            thumbScroll.style.transitionDuration = '0s';
            item.style.transitionDuration = '0s';
        }

        //
        item.addEventListener('touchmove', handleMove);
        item.addEventListener('touchend',handleUp);

    }

    item.addEventListener("touchstart",func);
});






const collectionSet = document.querySelector(".collection__frame > .collection");



collectionSet.addEventListener('touchstart', (event) => {

    itemPre = document.querySelector("." + CSS.escape(collectionSet.getAttribute('data-key') + "__pre"));
    itemNext = document.querySelector("." + CSS.escape(collectionSet.getAttribute('data-key') + "__next"));

        SetUp2();
        const detailX = event.touches[0].clientX;
        const detailY = event.touches[0].clientY;
        const initialPositionThumb = thumbScroll.offsetLeft;
        const initialPositionCollection = getInitPositionCollection();
        const handleMove = (event2) => {
            const position = event2.touches[0].clientX - detailX;
            let positionY = event2.touches[0].clientY - detailY;
            if(positionY < 0) {
                positionY = positionY * -1;
            }
            if(positionY > 20) {
                collectionSet.removeEventListener("touchmove", handleMove);
            }else  {
                event2.preventDefault();
                thumbScroll.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
                collectionSet.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
                savePosition = position;
            }
        }
        const  handleUp = () => {
            collectionSet.removeEventListener("touchmove",handleMove);
            collectionSet.removeEventListener("touchend",handleUp);
            if(savePosition > 0) {
                percentPassItem = 85;
            } else  {
                percentPassItem = 15;
            }
            handlePositionMouseUpCollect(collectionSet, thumbScroll);
            handlePositionMouseUpThumb(thumbScroll);
            thumbScroll.style.transitionDuration = '0s';
            collectionSet.style.transitionDuration = '0s';
        }
    collectionSet.addEventListener('touchmove', handleMove);
    collectionSet.addEventListener('touchend',handleUp);

    });








const outfitSet = document.querySelector(".styling__below--swiper");
outfitSet.addEventListener('touchstart', (event) => {

    itemPre = document.querySelector("." + CSS.escape(outfitSet.getAttribute('data-key') + "__pre"));
    itemNext = document.querySelector("." + CSS.escape(outfitSet.getAttribute('data-key') + "__next"));

    SetUp3();
    const detailX = event.touches[0].clientX;
    const detailY = event.touches[0].clientY;
    const initialPositionThumb = thumbScroll.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();

    const handleMove = (event2) => {
        const position = event2.touches[0].clientX - detailX;
        let positionY = event2.touches[0].clientY - detailY;
        if(positionY < 0) {
            positionY = positionY * -1;
        }
        if(positionY > 20) {
            outfitSet.removeEventListener("touchmove", handleMove);
        }else  {
            event2.preventDefault();
            thumbScroll.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            outfitSet.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
            savePosition = position;
        }
    }
    const  handleUp = () => {
        outfitSet.removeEventListener("touchmove",handleMove);
        outfitSet.removeEventListener("touchend",handleUp);
        if(savePosition >= 0) {
            percentPassItem = 85;
        } else  {
            percentPassItem = 15;
        }
        handlePositionMouseUpCollect(outfitSet, thumbScroll);
        handlePositionMouseUpThumb(thumbScroll);
        thumbScroll.style.transitionDuration = '0s';
        outfitSet.style.transitionDuration = '0s';
    }
    outfitSet.addEventListener('touchmove', handleMove);
    outfitSet.addEventListener('touchend',handleUp);

});









const feedBackSet = document.querySelector(".feedback__below--swiper");
feedBackSet.addEventListener('touchstart', (event) => {

    itemPre = document.querySelector("." + CSS.escape(feedBackSet.getAttribute('data-key') + "__pre"));
    itemNext = document.querySelector("." + CSS.escape(feedBackSet.getAttribute('data-key') + "__next"));

    SetUp4();
    const detailX = event.touches[0].clientX;
    const detailY = event.touches[0].clientY;
    const initialPositionThumb = thumbScroll.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();

    const handleMove = (event2) => {
        const position = event2.touches[0].clientX - detailX;
        let positionY = event2.touches[0].clientY - detailY;
        if(positionY < 0) {
            positionY = positionY * -1;
        }
        if(positionY > 20) {
            feedBackSet.removeEventListener("touchmove", handleMove);
        }else  {
            event2.preventDefault();
            thumbScroll.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            feedBackSet.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
            savePosition = position;
        }
    }
    const  handleUp = () => {
        feedBackSet.removeEventListener("touchmove",handleMove);
        feedBackSet.removeEventListener("touchend",handleUp);
        if(savePosition > 0) {
            percentPassItem = 85;
        } else  {
            percentPassItem = 15;
        }
        handlePositionMouseUpCollect(feedBackSet, thumbScroll);
        handlePositionMouseUpThumb(thumbScroll);
        thumbScroll.style.transitionDuration = '0s';
        feedBackSet.style.transitionDuration = '0s';
    }


    feedBackSet.addEventListener('touchmove', handleMove);
    feedBackSet.addEventListener('touchend',handleUp);

});






















