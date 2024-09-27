$(window).ready(() => {
    setUpSwiper();
    itemCollect  = document.querySelector(".collect__item-parent");
    itineraryThumb = thumbOutfitParent.offsetWidth - thumbOutfit.offsetWidth;
})

const thumbOutfitParent = document.querySelector(".swiper__thumb");
const thumbOutfit = document.querySelector(".swiper__thumb--scroll");
let  itemCollect = null;
let  itineraryThumb




function setUpSwiper() {
    const  array = document.querySelectorAll(".collect__item");
    const  parent = document.querySelector(".swiper__collect--slide");
    parent.style.gridTemplateColumns = "repeat("+ array.length +", calc(100vw/4))"


    if(array.length < 4) {
        thumbOutfit.style.width = '100%';
        parent.style.justifyContent= 'center';
    }else  {
        const  width = (thumbOutfitParent.offsetWidth / (array.length - 3))
        thumbOutfit.style.width = width + "px";
    }


    function myFunction(x) {
        if (x.matches) { // If media query matches
            parent.style.gridTemplateColumns = "repeat("+ array.length +", calc(100vw/2))"


            if(array.length < 2) {
                thumbOutfit.style.width = '100%';
                parent.style.justifyContent= 'center';
            }else  {
                const  width = (thumbOutfitParent.offsetWidth / (array.length - 1))
                thumbOutfit.style.width = width + "px";
                parent.style.justifyContent= 'start';
            }
        }
    }

    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);


    array.forEach((e) => {
        const  option = handleLV(e.getAttribute("data-option"));
        e.setAttribute("href", "/products?option=" + option);
    })
}

function handleLV(data)
{
    const level1 = data.replaceAll(" ", "-");
    return level1.replaceAll("/", "-");
}


const getInitPositionCollection = () => {
    const  percentUneven = itemCollect.offsetWidth / thumbOutfit.offsetWidth;
    return thumbOutfit.offsetLeft * percentUneven;
}
const getaPotionThumbOutfit = (positionCurrent, initialPosition) => {
    return initialPosition +  -positionCurrent;
}
const getaPotionCollectOutfit = (positionCurrent, initialPosition) => {
    const  time = 0;
    const  percentUneven = itemCollect.offsetWidth / thumbOutfit.offsetWidth;
    return -initialPosition + (positionCurrent * percentUneven);
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
    thumbElement.style.transaction = "0.5s";
}

let managerMouseMove = true;
let manageMouseUp = true;

function handleMouseDownOutfit(e, thisElement) {
    e.preventDefault();
    managerMouseMove = true;
    const detailX = e.clientX;
    const initialPositionThumb = thumbOutfit.offsetLeft;
    const initialPositionCollection =  getInitPositionCollection();
    function  handleMouseMove(event) {
        event.preventDefault();
        manageMouseUp = false;
        if(managerMouseMove === true) {
            const position = event.clientX - detailX;
            thumbOutfit.style.left = getaPotionThumbOutfit(position, initialPositionThumb) + 'px';
            thisElement.style.transform = "translate3d(" + getaPotionCollectOutfit(position, initialPositionCollection) + "px, 0 , 0)";
        }
    }

    function handleMouseUp(e) {
        thisElement.removeEventListener('mousemove', handleMouseMove);
        thisElement.removeEventListener('mouseup', handleMouseUp);
        handlePositionMouseUpCollectOutFit(thisElement, thumbOutfit);
        handlePositionMouseUpThumb(thumbOutfit);
        thumbOutfit.style.transitionDuration = '0s';
        thisElement.style.transitionDuration = '0s';
        if(manageMouseUp === false) {
            thisElement.addEventListener('click', handleClick)
        }
    }
    function  handleClick(e) {
        e.preventDefault();
        thisElement.removeEventListener("click", handleClick)
    }

    thisElement.addEventListener('mousemove', handleMouseMove);
    thisElement.addEventListener('mouseup', handleMouseUp);

}
function handleMouOverOutfit() {
    manageMouseUp = true;
}

function handleMouseOutOutfit() {
    managerMouseMove = false;
}

const handlePositionMouseUpCollectOutFit = (thisElement, thumb) => {
    const  percentUneven = itemCollect.offsetWidth / thumb.offsetWidth;
    if(thumb.offsetLeft < 0) {
        thisElement.style.transform = "translate3d(0, 0 , 0)";
    } else
    if(thumb.offsetLeft > itineraryThumb) {
        const width = itineraryThumb * percentUneven
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    } else  {
        const positionCurrent =  thumb.offsetLeft * percentUneven;
        const extra = positionCurrent  % itemCollect.offsetWidth;
        const  percent = extra * 100 / itemCollect.offsetWidth;
        if(percent > 50) {
            const  data = ((100 - percent) / 100 ) * itemCollect.offsetWidth;
            const  width = positionCurrent + data;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        } else  {
            const widthMinus = (percent / 100) * itemCollect.offsetWidth;
            const width = (thumb.offsetLeft * percentUneven) - widthMinus;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        }
    }
    thisElement.style.transitionDuration = '0.4s';
}
