
let itineraryThumb = null;
$(window).ready(() => {
    setUpSwiper();
    setUpDescribe();
    itineraryThumb  = parentThumb.offsetWidth - thumb.offsetWidth;
})

function setUpDescribe() {
    const item = document.querySelector(".detailCollection__describe");
    item.innerHTML = detail;
}


let managerMouseMove = true;
let manageMouseUp = true;

function  setUpSwiper() {
    const thumb = document.querySelector(".swiper__thumb--scroll");
    const  parentThumb = document.querySelector(".swiper__thumb");
    const  main = document.querySelector(".swiper__frame--List");
    const  array = document.querySelectorAll(".swiper__frame--List > .List__card")
    main.style.gridTemplateColumns = "repeat("+array.length +", calc(100vw/ 4))";
    const withActive = parentThumb.offsetWidth / ((array.length - 3));
    thumb.style.width = withActive + 'px';

    function myFunction(x) {
        if (x.matches) { // If media query matches
            main.style.gridTemplateColumns = "repeat("+array.length +", calc(100vw/ 2))";
            const withActive = parentThumb.offsetWidth / ((array.length - 1));
            thumb.style.width = withActive + 'px';
        }
    }

// Create a MediaQueryList object
    var x = window.matchMedia("(max-width: 500px)")

// Call listener function at run time
    myFunction(x);
}

const  parentThumb = document.querySelector(".swiper__thumb");
const thumb = document.querySelector(".swiper__thumb--scroll");
const item = document.querySelector(".List__card");

const getInitPositionCollection = () => {
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    return thumb.offsetLeft * percentUneven;
}

const right = document.querySelector(".swiper__next");
const left = document.querySelector(".swiper__pre");
const getaPotionCollect = (positionCurrent, initialPosition) => {
    if(right != null) {
        right.style.display = "block";
    }
    if(right != null) {
        left.style.display = "block";
    }
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    return -initialPosition + (positionCurrent * percentUneven);
}

const  handleTimeCollect= (Position,thisElement) =>  {
    let time = 0 ;
    if(thumb.offsetLeft < 0) {
        time = time + 0.2;
    } else
    if(thumb.offsetLeft > itineraryThumb) {
    } else  {
        time = 0;
    }
    thisElement.style.transitionDuration =  time + 's';
}


const getaPotionThumb = (positionCurrent, initialPosition) => {
    return initialPosition +  -positionCurrent;
}

const handlePositionMouseUpCollect = (thisElement, thumb) => {
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    if(thumb.offsetLeft < 0) {
        thisElement.style.transform = "translate3d(0, 0 , 0)";
        const left = document.querySelector(".swiper__pre");
        if(left != null) {
            left.style.display = "none";
        }
    } else
    if(thumb.offsetLeft > itineraryThumb) {
        const width = itineraryThumb * percentUneven
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        // hidden right
        const right = document.querySelector(".swiper__next");
        if(right != null) {
            right.style.display = "none";
        }
    } else  {
        const positionCurrent =  thumb.offsetLeft * percentUneven;
        const extra = positionCurrent  % item.offsetWidth;
        const  percent = extra * 100 / item.offsetWidth;
        if(percent > 50) {
            const  data = ((100 - percent) / 100 ) * item.offsetWidth;
            const  width = positionCurrent + data;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        } else  {
            const widthMinus = (percent / 100) * item.offsetWidth;
            const width = (thumb.offsetLeft * percentUneven) - widthMinus;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        }
    }
    thisElement.style.transitionDuration = '0.4s';
}

const handlePositionMouseUpThumb = (thumbElement) => {
    if(thumbElement.offsetLeft < 0) {
        thumbElement.style.left = 0 + 'px';
    }else
    if(thumbElement.offsetLeft > itineraryThumb) {
        thumbElement.style.left = itineraryThumb + 'px';
    } else  {
        const extra = thumbElement.offsetLeft % thumbElement.offsetWidth;
        const  percent = extra * 100 / thumbElement.offsetWidth;
        if(percent > 50) {
            const  width = ((100 - percent) / 100) * thumbElement.offsetWidth;
            const widthUse = width + thumbElement.offsetLeft;
            if(widthUse === itineraryThumb ) {
                right.style.display = "none";
            }
            thumbElement.style.left = widthUse + 'px';
            thumbElement.style.transitionDuration = '0.4s';
        } else  {
            const widthMinus = (percent / 100) * thumbElement.offsetWidth;
            const widthUse = thumbElement.offsetLeft - widthMinus;
            if(widthUse === 0 ) {
                left.style.display= "none";
            }
            thumbElement.style.left =  widthUse + 'px';
            thumbElement.style.transitionDuration = '0.4s';
        }
    }
    thumbElement.style.transitionDuration = '0.4s';
}




function handleMouseDownDetail(e, thisElement) {
    e.preventDefault();
    managerMouseMove = true;

    const detailX = e.clientX;
    const detailY = e.clientY;
    const initialPositionThumb = thumb.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();

    function  handleMouseMouse(e) {
        manageMouseUp = false;
        let positionY = e.clientY - detailY;
        if(positionY < 0) {
            positionY = positionY * -1;
        }
        if(managerMouseMove === true && positionY < 30) {
            const position = e.clientX - detailX;
            thumb.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            thisElement.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
            handleTimeCollect(getaPotionCollect(position, initialPositionCollection), thisElement);
        }
    }
    function handleMouseUp(e) {
        thisElement.removeEventListener('mousemove', handleMouseMouse);
        thisElement.removeEventListener('mouseup', handleMouseUp);
        handlePositionMouseUpCollect(thisElement, thumb);
        handlePositionMouseUpThumb(thumb);


        thumb.style.transitionDuration = '0s';
        thisElement.style.transitionDuration = '0s';
        if(manageMouseUp === false) {
            thisElement.addEventListener('click', handleClick)
        }
    }

    function  handleClick(e) {
        e.preventDefault();
        thisElement.removeEventListener("click", handleClick)
    }

    thisElement.addEventListener('mousemove', handleMouseMouse)
    thisElement.addEventListener('mouseup', handleMouseUp)
}


const feedBackSet = document.querySelector(".swiper__frame--List");

feedBackSet.addEventListener('touchstart', (event) => {
    const detailX = event.touches[0].clientX;
    const detailY = event.touches[0].clientY;
    const initialPositionThumb = thumb.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();

    const handleMove = (event2) => {
        event2.preventDefault();
        const position = event2.touches[0].clientX - detailX;
        let positionY = event2.touches[0].clientY - detailY;
        if(positionY < 0) {
            positionY = positionY * -1;
        }
        if(positionY > 20) {
            feedBackSet.removeEventListener("touchmove", handleMove);
        }else  {
            thumb.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            feedBackSet.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)";
            handleTimeCollect(getaPotionCollect(position, initialPositionCollection), feedBackSet);
        }
    }
    const  handleUp = () => {
        feedBackSet.removeEventListener("touchmove",handleMove);
        feedBackSet.removeEventListener("touchend",handleUp);

        handlePositionMouseUpCollect(feedBackSet, thumb);
        handlePositionMouseUpThumb(thumb);

        thumb.style.transitionDuration = '0s';
        feedBackSet.style.transitionDuration = '0s';
    }

    feedBackSet.addEventListener('touchmove', handleMove);
    feedBackSet.addEventListener('touchend',handleUp);

});




function handleMouOver() {
    manageMouseUp = true;
}

function handleMouseOut() {
    managerMouseMove = false;
}

function handleNextItem() {
    const collection = document.querySelector(".swiper__frame--List");
    const thumb = document.querySelector(".swiper__thumb--scroll");
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
   const  width = (thumb.offsetLeft * percentUneven)  + item.offsetWidth;
   const widthThumb = thumb.offsetLeft + thumb.offsetWidth;
   if(thumb.offsetLeft < itineraryThumb) {
       collection.style.transform = "translate3d(-"+width+"px, 0 , 0)";
       thumb.style.left = widthThumb + "px";
       collection.style.transitionDuration = '0.4s';
       thumb.style.transitionDuration = '0.4s';
   }
}

function handlePreItem() {
    const collection = document.querySelector(".swiper__frame--List");
    const thumb = document.querySelector(".swiper__thumb--scroll");
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    const  width = (thumb.offsetLeft * percentUneven)  - item.offsetWidth;
    const widthThumb = thumb.offsetLeft - thumb.offsetWidth;
    if(thumb.offsetLeft > 0) {
        collection.style.transform = "translate3d(-" + width + "px, 0 , 0)";
        thumb.style.left = widthThumb + "px";
        collection.style.transitionDuration = '0.4s';
        thumb.style.transitionDuration = '0.4s';
    }
}

