let container = document.querySelector(".zoom__frame--list");
const scrollThumb = document.querySelector(".zoom__move--bar");
const scrollFrame = document.querySelector(".zoom__move");
const  arrayItem = document.querySelectorAll(".frame__item");
const  item = document.querySelector(".frame__item");
let itineraryThumb  = null;
let managerMouseMove = true;
let  widthThumbSwiperFactZoom = null;

function setUp() {
    container.style.gridTemplateColumns = "repeat(" + arrayItem.length + ", calc(100% /3))";
    scrollThumb.style.width = (scrollFrame.offsetWidth / (arrayItem.length - 2)) + 'px';
    itineraryThumb =  scrollFrame.offsetWidth - scrollThumb.offsetWidth;

    function myFunction(x) {
        if (x.matches) { // If media query matches
            container.style.gridTemplateColumns = "repeat(" + 1 + ", 100vw)";
            container.style.gridTemplateRows = "repeat(" + arrayItem.length + ", calc(100vh / 1.5))";
            scrollThumb.style.width = (scrollFrame.offsetWidth / ((arrayItem.length - 0.5))) + 'px';
            itineraryThumb =  scrollFrame.offsetWidth - scrollThumb.offsetWidth;
            widthThumbSwiperFact = itineraryThumb / (arrayItem.length  - 1.5);
        }
    }
    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);
}
setUp();

const percentSet = ()=> {
    return item.offsetHeight / widthThumbSwiperFact;
}

const getInitPositionCollection = () => {
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    return scrollThumb.offsetLeft * percentUneven;
}

const getInitPositionCollectionMobile = () => {
    const  percentUneven = window.innerHeight / scrollThumb.offsetWidth;
    return scrollThumb.offsetLeft * percentSet();
}



const getaPotionCollect = (positionCurrent, initialPosition) => {
    return -initialPosition + (positionCurrent);
}
const getaPotionThumb = (positionCurrent, initialPosition) => {
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    return initialPosition +  -(positionCurrent / percentUneven);
}
const getaPotionThumbMobile = (positionCurrent, initialPosition) => {
    const  percentUneven = window.innerHeight / scrollThumb.offsetWidth;
    return initialPosition +  -(positionCurrent / percentSet());
}



function  handleMouseDownZoom(event, thisElement) {
    event.preventDefault();
    managerMouseMove =true;
    const  detailX = event.clientX;
    const initialPositionThumb = scrollThumb.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();
    function handleMouseMove(e) {
        const position = e.clientX - detailX;
        if(managerMouseMove === true) {
            scrollThumb.style.transitionDuration = '0s';
            thisElement.style.transitionDuration = '0s';
            scrollThumb.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            thisElement.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)"
        }
    }

    function handleMouseUp() {
        thisElement.removeEventListener('mousemove', handleMouseMove);
        thisElement.removeEventListener('mouseup', handleMouseUp);

        handlePositionMouseUpCollect(thisElement, scrollThumb);
        handlePositionMouseUpThumb(scrollThumb);
    }

    thisElement.addEventListener('mousemove', handleMouseMove);
    thisElement.addEventListener('mouseup', handleMouseUp)
}

const handlePositionMouseUpCollectMobile = (thisElement, thumb) => {
    const  percentUneven =percentSet();
    if(thumb.offsetLeft < 0) {
        thisElement.style.transitionDuration = '0.2s';
        thisElement.style.transform = "translate3d(0, 0 , 0)";
    } else
    if(thumb.offsetLeft > itineraryThumb) {
        thisElement.style.transitionDuration = '0.2s';
        const width = itineraryThumb * percentUneven
        thisElement.style.transform = "translate3d(0, -"+width+"px , 0)";
    } else  {

        thisElement.style.transitionDuration = '0.2s';
        const positionCurrent =  thumb.offsetLeft * percentUneven;
        const extra = positionCurrent  % item.offsetHeight;
        const  percent = extra * 100 / item.offsetHeight;
        if(percent > 50) {
            const  data = ((100 - percent) / 100 ) * item.offsetHeight;
            const  width = positionCurrent + data;
            thisElement.style.transform = "translate3d(0, -"+width+"px , 0)";
        } else  {
            const widthMinus = (percent / 100) * item.offsetHeight;
            const width = (thumb.offsetLeft * percentUneven) - widthMinus;
            thisElement.style.transform = "translate3d(0 , -"+width+"px , 0)";
        }
    }
}

const handlePositionMouseUpCollect = (thisElement, thumb) => {
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    if(thumb.offsetLeft < 0) {
        thisElement.style.transitionDuration = '0.2s';
        thisElement.style.transform = "translate3d(0, 0 , 0)";
    } else
    if(thumb.offsetLeft > itineraryThumb) {
        thisElement.style.transitionDuration = '0.2s';
        const width = itineraryThumb * percentUneven
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    } else  {

        thisElement.style.transitionDuration = '0.2s';
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
}

function handleMouseOut() {
    managerMouseMove = false;

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
            thumbElement.style.left = widthUse + 'px';
        } else  {
            const widthMinus = (percent / 100) * thumbElement.offsetWidth;
            const widthUse = thumbElement.offsetLeft - widthMinus;
            thumbElement.style.left =  widthUse + 'px';
        }
    }
    
}

const  handleViewZoom = () => {
    const  zoom = document.querySelector(".zoom");
    zoom.classList.add("active__zoom");
}

const  closeViewZoom = () => {
    const  zoom = document.querySelector(".zoom");
    zoom.classList.remove("active__zoom");
}


















const itemZoom = document.querySelector(".zoom__frame--list");

itemZoom.addEventListener('touchstart', (event) => {

    const detailY = event.touches[0].clientY;
    const initialPositionThumb = scrollThumb.offsetLeft;
    const initialPositionCollection = getInitPositionCollectionMobile();

    const handleMove = (event2) => {
        event2.preventDefault();
        scrollThumb.style.transitionDuration = '0s';
        itemZoom.style.transitionDuration = '0s';
        const position = event2.touches[0].clientY - detailY;
        scrollThumb.style.left = getaPotionThumbMobile(position, initialPositionThumb) + 'px';
        itemZoom.style.transform = "translate3d(0," + getaPotionCollect(position, initialPositionCollection) + "px, 0)"
    }
    const  handleUp = () => {
        itemZoom.removeEventListener("touchmove",handleMove);
        itemZoom.removeEventListener("touchend",handleUp);
        handlePositionMouseUpCollectMobile(itemZoom, scrollThumb);
        handlePositionMouseUpThumb(scrollThumb);
    }


    itemZoom.addEventListener('touchmove', handleMove);
    itemZoom.addEventListener('touchend',handleUp);

});
