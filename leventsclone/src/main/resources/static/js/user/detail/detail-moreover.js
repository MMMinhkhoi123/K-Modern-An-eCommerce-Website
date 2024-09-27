const parentOpen = document.querySelector(".parent-open");
const thumbMove = document.querySelector(".slide-open__move--bar");
let thumbMove__hidden = document.querySelector(".slide-open__move--bar-hidden");
const thumbMoveChild = document.querySelector(".slide-open__move--bar-child");
const parentThumbMoveOpen  = document.querySelector(".slide-open__move");
let listSlide = document.querySelectorAll(".slide-open__current > div");
const  Slide = document.querySelector(".slide-open__current");

const  array = document.querySelectorAll(".slide-open__slide--item");
let  heightInitThumb = parentThumbMoveOpen.offsetHeight / (array.length);
const caseIndex = document.querySelector(".detail-product__img");
let  spaceMax = null;

$(window).ready(() => {
    setUpx();
})


function setUpDetailMobile() {
    function myFunction(x) {
        if (x.matches) { // If media query matches
            setUpMobile();
            runData();
        }
    }
    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);
}

function setUpMobile() {
    const parentThumbScroll = document.querySelector(".slide-open__move");
    const thumbScroll  =  document.querySelector(".slide-open__move--bar");
    const list = document.querySelector(".slide-open__current");
    const data = document.querySelectorAll(".slide-open__current > div");
    list.style.gridTemplateColumns = "repeat(" + data.length + ", 100vw )";
    const withActive = parentThumbScroll.offsetWidth / ((data.length));
    thumbScroll.style.width = withActive + 'px';
    thumbMove__hidden.style.width = withActive + 'px';
    thumbMove__hidden.style.height = 100 + '%';
    heightInitThumb = parentThumbMoveOpen.offsetWidth / (array.length);
    spaceMax = parentThumbMoveOpen.offsetWidth - thumbMove__hidden.offsetWidth;

}

function runData() {
    const item = document.querySelector(".slide-open__current");

    item.addEventListener('touchstart', (event) => {

        const detailX = event.touches[0].clientX;
        const detailY = event.touches[0].clientY;

        let currentPosition = 0;

        let positionCollectCurrent =  getPositionCurrentCollectMobile();

        let positionThumbCurrent = thumbMove.offsetWidth;

        let positionThumbCurrentHidden = thumbMove__hidden.offsetLeft;
        const handleMove = (event2) => {
            event2.preventDefault();
            const position = event2.touches[0].clientX - detailX;
            let positionY = event2.touches[0].clientY - detailY;
                currentPosition = position;

                if(positionY < 0) {
                    positionY = positionY * -1;
                }
                if(positionY > 20) {
                    item.removeEventListener("touchmove", handleMove)
                }else  {
                    handleCollectAddPendMobile(thumbMove__hidden, position);
                    parentOpen.style.transitionDuration  = "0s";
                    parentOpen.style.transform = "translate3d(" + getPositionSlideMobile(position, positionCollectCurrent)+"px, 0px , 0px)";
                    handleDataMobile(thumbMove__hidden, position);
                    thumbMove__hidden.style.left = getPositionThumbHiddenMobile(position,positionThumbCurrentHidden) + "px";
                    thumbMove.style.width = getPositionThumbMobile(thumbMove, position, positionThumbCurrent)+ "px";
                }
        }
        const  handleUp = () => {
            item.removeEventListener("touchend", handleUp);
            item.removeEventListener("touchmove",handleMove);
            item.removeEventListener("touchend",handleUp);

            handleMouseUpCollectMobile(currentPosition);
            handleMouseUpThumbHidenMobile(currentPosition);
        }


        item.addEventListener('touchmove', handleMove);
        item.addEventListener('touchend',handleUp);
    });
}





















function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function setUpx() {
const  array = document.querySelectorAll(".slide-open__current > div");
    thumbMove.style.height = parentThumbMoveOpen.offsetHeight / (array.length) + "px";
    thumbMove__hidden.style.height = parentThumbMoveOpen.offsetHeight / (array.length) + "px";
    thumbMoveChild.style.height = parentThumbMoveOpen.offsetHeight / (array.length) + "px";
    const price = document.querySelectorAll(".specific__price");
    price.forEach((e) => {
        e.innerHTML =  assetFormat(Math.floor(Number(e.getAttribute("data-price"))).toString()) + " VND";// 1,234,567,890
    })
    spaceMax = parentThumbMoveOpen.offsetHeight - thumbMove__hidden.offsetHeight;
    setUpDetailMobile()
}
let count = 1;


function getPositionThumbChild(data, innit) {
    return (innit + (- data));
}
function getPositionSlideChild(data, innit) {
    return (innit + (- data));
}

function getPercentNeven() {
   return  thumbMoveChild.offsetHeight / 100;
}


function moveChildSlide(x, thisElement) {
    // x.preventDefault();
    // const detailY = x.clientY;
    // const innitPositionThumb = thumbMoveChild.offsetTop;
    // function handleMouseMove(e) {
    //     const position = e.clientY - detailY;
    //     thumbMoveChild.style.top = getPositionThumbChild(position, innitPositionThumb) +  "px";
    //     thisElement.style.transform = "translate3d(0px," +  getPositionSlideChild(position,) +"px , 0px)";
    // }
    //
    // function handleMouseUp(e) {
    //     thisElement.removeEventListener('mouseup', handleMouseUp);
    //     thisElement.removeEventListener('mousemove', handleMouseMove);
    // }
    // thisElement.addEventListener('mousemove', handleMouseMove)
    // thisElement.addEventListener('mouseup', handleMouseUp)
}

function checkAllIndex(data) {
    // console.log(heightInitThumb + " / " + data);
    const  classActive = "active"
    let count = Math.floor(data / heightInitThumb);
    array.forEach((item) => {
        if(item.getAttribute("class").includes(classActive)) {
            item.classList.remove(classActive);
        }
    })

    count = count - 1;
    const item = document.querySelector("#" + CSS.escape('slide' + count));
    item.classList.add(classActive);
}

let managerMoveMove = false;



let processAddPre  = false;
let processAddNext  = false;


let width = 0;
let indexSetPre =  listSlide.length - 1;
let indexSetNext =  0;


let allowMoveNextThumb = null;
let allowMovePreThumb = null;


let startScroll = false;


function  getPositionSlideMobile(data, initPosition) {
    if(processAddPre === true) {
        return (-initPosition - parentThumbMoveOpen.offsetWidth) + (data);
    } if(processAddNext === true) {
        return (-initPosition + parentThumbMoveOpen.offsetWidth) + (data);
    }
    else  {
        return -initPosition + (data);
    }
}
function  getPositionSlide(data, initPosition) {
    if(processAddPre === true) {
        return (-initPosition - 600) + (data);
    } if(processAddNext === true) {
        return (-initPosition + 600) + (data);
    }
    else  {
        return -initPosition + (data);
    }
}


function getPercentMobile() {
    const distance = parentThumbMoveOpen.offsetWidth - heightInitThumb;
    const faStance=  distance / (listSlide.length - 1);
    return  parentThumbMoveOpen.offsetWidth / faStance;
}

function getPercentUneven() {
    const distance = parentThumbMoveOpen.offsetHeight - heightInitThumb;
    const faStance=  distance / (listSlide.length - 1);
    return  600 / faStance;
}
function  getPositionThumbHiddenMobile(data, initPosition) {
    return initPosition +  -(data / getPercentMobile());
}

function  getPositionThumb(thumb,data, initPosition) {

    if(processAddPre === true) {
        if(data > 0) {
            if(allowMovePreThumb === true) {
                return parentThumbMoveOpen.offsetHeight;
            }else if(allowMovePreThumb === false) {
                return heightInitThumb;
            }
        }
    } if(processAddNext === true) {
        if(data < 0) {
            if(allowMoveNextThumb === true) {
                return heightInitThumb;
            }else if(allowMoveNextThumb === false) {
                return parentThumbMoveOpen.offsetHeight;
            }
        }
    }
    else  {

        if(data < 0) {
            if(allowMoveNextThumb === true) {

                return initPosition + heightInitThumb;
            }else if(allowMoveNextThumb === false) {
                return initPosition;
            }
        } else  {
            if(allowMovePreThumb === true) {
                return initPosition - heightInitThumb;
            }else if(allowMovePreThumb === false) {
                return initPosition;
            }
        }

    }

    thumb.style.transitionDuration  = "0.9s";
}



function  getPositionThumbMobile(thumb,data, initPosition) {
    if(processAddPre === true) {
        if(data > 0) {
            if(allowMovePreThumb === true) {
                return parentThumbMoveOpen.offsetWidth;
            }else if(allowMovePreThumb === false) {
                return heightInitThumb;
            }
        }
    }
    if(processAddNext === true) {
        if(data < 0) {
            if(allowMoveNextThumb === true) {
                return heightInitThumb;
            }else if(allowMoveNextThumb === false) {
                return parentThumbMoveOpen.offsetWidth;
            }
        }
    }
        if(data < 0) {
            if(allowMoveNextThumb === true) {
                return initPosition + heightInitThumb;
            }else if(allowMoveNextThumb === false) {
                return initPosition;
            }
        } else  {
            if(allowMovePreThumb === true) {
                return initPosition - heightInitThumb;
            }else if(allowMovePreThumb === false) {
                return initPosition;
            }
        }
    thumb.style.transitionDuration  = "0.9s";
}
function  getPositionThumbHidden(data, initPosition) {
    return initPosition +  -(data / getPercentUneven());
}

function getIndexPre() {
    let  dataResult = null;
    listSlide.forEach((e) => {
        if(Number(e.getAttribute('data-index')) === indexSetPre) {
            dataResult = e;
            indexSetPre = Number(e.getAttribute('data-index'));
        }
    })
    return dataResult;
}

function getIndexNext() {
    let  dataResult = null;
    listSlide.forEach((e) => {
        if(Number(e.getAttribute('data-index')) === indexSetNext) {
            dataResult = e;
            indexSetNext = Number(e.getAttribute('data-index'));
        }
    })
    return dataResult;
}





// constantly run
function handleCollectAddPend(thumb, data) {
    if(thumb.offsetTop < 0) {

        if(processAddPre === false) {
            addPreItem()
            processAddPre = true;
        }

    } else if(thumb.offsetTop > spaceMax) {
        if(processAddNext === false) {
            addNextItem()
            processAddNext = true;
        }
    } else  {

    }
}

function handleCollectAddPendMobile(thumb, data) {
    if(thumb.offsetLeft < 0) {

        if(processAddPre === false) {
            addPreItem()
            processAddPre = true;
        }

    } else if(thumb.offsetLeft > spaceMax) {
        if (processAddNext === false) {
            addNextItem()
            processAddNext = true;
        }
    }
}



function addPreItem() {
    const nodeApp = getIndexPre();
    Slide.prepend(nodeApp);
}

function addNextItem() {
    const nodeApp = getIndexNext()
    Slide.appendChild(nodeApp);
}


function choiceImg(index) {
    const data  =  (heightInitThumb * (index + 1));
    thumbMove__hidden.style.top =  (data - heightInitThumb) + "px";
    thumbMove.style.height = data + "px";
    parentOpen.style.transform = "translate3d(0px, -" + ((data * getPercentUneven()) - 600)  +"px , 0px)";
    thumbMove.style.transitionDuration  = "0.6s";
    parentOpen.style.transitionDuration  = "0.6s";
    setTimeout(() => {
        checkAllIndex(thumbMove.offsetHeight);
    }, 1000);
}


function handleData(thumb, data) {
    const getExtra = thumb.offsetTop % thumbMove__hidden.offsetHeight;
    let percent = (getExtra / thumbMove__hidden.offsetHeight) * 100;
    if(percent < 0) {
        percent *= -1;
    }
    if(thumb.offsetTop < 0) {

        if(data > 0) {
            if(percent > 50) {
                allowMovePreThumb = true;
            }else  {
                allowMovePreThumb = false;
            }
        } else  {
            if(percent < 50) {
                allowMoveNextThumb = true;
            } else  {
                allowMoveNextThumb = false;
            }
        }

    }
    else if(thumb.offsetTop > spaceMax)  {
        if(data < 0) {
            if(percent > 50) {
                allowMoveNextThumb = true;
            }else  {
                allowMoveNextThumb = false;
            }
        } else  {
            if(percent < 50) {
                allowMovePreThumb = true;
            } else  {
                allowMovePreThumb = false;
            }
        }
    }
    else if(thumb.offsetTop !== 0)  {
        if(data < 0) {
            if(percent > 50) {
                allowMoveNextThumb = true;
            }else  {
                allowMoveNextThumb = false;
            }
        } else  {
            if(percent < 50) {
                allowMovePreThumb = true;
            }else  {
                allowMovePreThumb = false;
            }
        }
    }
}



function handleDataMobile(thumb, data) {
    let percent = thumb.offsetLeft % thumbMove__hidden.offsetWidth;
    if(percent < 0) {
        percent *= -1;
    }
    if(thumb.offsetLeft < 0) {

        if(data > 0) {
            if(percent > 5) {
                allowMovePreThumb = true;
            }else  {
                allowMovePreThumb = false;
            }
        } else  {
            if(percent < 95) {
                allowMoveNextThumb = true;
            } else  {
                allowMoveNextThumb = false;
            }
        }

    }
    else if(thumb.offsetLeft > spaceMax)  {
        if(data < 0) {
            if(percent > 5) {
                allowMoveNextThumb = true;
            }else  {
                allowMoveNextThumb = false;
            }
        } else  {
            if(percent < 95) {
                allowMovePreThumb = true;
            } else  {
                allowMovePreThumb = false;
            }
        }
    }
    else if(thumb.offsetLeft !== 0)  {
        if(data < 0) {
            if(percent > 5) {
                allowMoveNextThumb = true;
            }else  {
                allowMoveNextThumb = false;
            }
        } else  {
            if(percent < 95) {
                allowMovePreThumb = true;
            }else  {
                allowMovePreThumb = false;
            }
        }
    }
}



function  getPositionCurrentCollect() {
    return thumbMove__hidden.offsetTop * getPercentUneven();
}
function  getPositionCurrentCollectMobile() {
    return thumbMove__hidden.offsetLeft * getPercentMobile();
}



function lockA () {
    var xPos = document.querySelector('.container').scrollLeft;
    var yPos = document.querySelector('.container').scrollTop ;
    document.querySelector('.container').onscroll = () => document.querySelector('.container').scroll(xPos, yPos);
}
function unlock () {
    document.querySelector('.container').onscroll = ""; }
function handleMoveOver() {
    lockA ();
}

function preSlide() {
    const dataTop = thumbMove__hidden.offsetTop - thumbMove__hidden.offsetHeight;
    parentOpen.style.transitionDuration  = "0s";
    if(dataTop >= 0) {
        thumbMove__hidden.style.top = dataTop + "px";
        thumbMove.style.height =  (thumbMove.offsetHeight - heightInitThumb) + "px"
        parentOpen.style.transform = "translate3d(0px, -" +  (dataTop * getPercentUneven())  +"px , 0px)";
        thumbMove.style.transitionDuration  = "0.9s";
        parentOpen.style.transitionDuration  = "0.9s";
        startScroll = false;
    } else  {
        addPreItem();
        parentOpen.style.transform = "translate3d(0px,-" + 600  +"px , 0px)";
        setTimeout(() => {
            thumbMove__hidden.style.top = spaceMax + "px";
            thumbMove.style.height =  parentOpen.offsetHeight + "px"
            parentOpen.style.transform = "translate3d(0px, " +  0  +"px , 0px)";
            thumbMove.style.transitionDuration  = "0.9s";
            parentOpen.style.transitionDuration  = "0.9s";
            setTimeout(() => {
                resetEnd();
                startScroll = false;
            }, 0.9 * 1000)
        }, 0.1 * 1000);
    }
}
function nextSlide() {
    const dataTop = thumbMove__hidden.offsetTop + thumbMove__hidden.offsetHeight;
    parentOpen.style.transitionDuration  = "0s";
    if(dataTop <= spaceMax) {
        thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop + thumbMove__hidden.offsetHeight) + "px";
        thumbMove.style.height =  (thumbMove.offsetHeight + heightInitThumb) + "px"
        parentOpen.style.transform = "translate3d(0px, -" +  (dataTop * getPercentUneven())  +"px , 0px)";
        thumbMove.style.transitionDuration  = "0.9s";
        parentOpen.style.transitionDuration  = "0.9s";
        startScroll = false;
    } else  {
        addNextItem();
        parentOpen.style.transform = "translate3d(0px,-" +   ((spaceMax * getPercentUneven()) - 600)  +"px , 0px)";
        setTimeout(() => {
            thumbMove__hidden.style.top = 0 + "px";
            thumbMove.style.height =  heightInitThumb + "px"
            parentOpen.style.transform = "translate3d(0px, -" +  ((spaceMax * getPercentUneven()))  +"px , 0px)";
            thumbMove.style.transitionDuration  = "0.9s";
            parentOpen.style.transitionDuration  = "0.9s";
            setTimeout(() => {
                resetBeginNext();
                startScroll = false;
            }, 0.9 * 1000)
        }, 0.1 * 1000);
    }
}


function  run() {

    // parentOpen.addEventListener('mouseleave', unlock);
    //
    // parentOpen.addEventListener('mouseover',handleMoveOver);

    parentOpen.addEventListener('mousedown',(x) => {
        x.preventDefault();

        managerMoveMove = true;

        const detailY = x.clientY;

        let currentPosition = 0;

        let positionCollectCurrent =  getPositionCurrentCollect();

        let positionThumbCurrent = thumbMove.offsetHeight;

        let positionThumbCurrentHidden = thumbMove__hidden.offsetTop;
        const handleMouseMove = (e) => {
                if(managerMoveMove === true) {
                    parentOpen.style.transitionDuration  = "0s";

                    const position = e.clientY - detailY;

                    if(position !==0) {
                        currentPosition = position;
                        handleCollectAddPend(thumbMove__hidden, position);
                        parentOpen.style.transform = "translate3d(0px, " +  getPositionSlide(position, positionCollectCurrent)  +"px , 0px)";
                        handleData(thumbMove__hidden, position);
                        thumbMove__hidden.style.top = getPositionThumbHidden(position, positionThumbCurrentHidden) + "px";
                        thumbMove.style.height = getPositionThumb(thumbMove, position, positionThumbCurrent) + "px";
                        setTimeout(() => {
                            checkAllIndex(thumbMove.offsetHeight);
                        }, 0.9 * 1000);
                    }
                }
        }


        const handleMouseUp = (e) => {
            parentOpen.removeEventListener('mouseup', handleMouseUp);
            parentOpen.removeEventListener('mousemove', handleMouseMove);
            if(currentPosition !== 0) {
                handleMouseUpCollect(currentPosition);
                handleMouseUpThumb(currentPosition);
                handleMouseUpThumbHiden(currentPosition);
            }
        }

        parentOpen.addEventListener('mousemove', handleMouseMove)
        parentOpen.addEventListener('mouseup', handleMouseUp)
        parentOpen.addEventListener('mouseleave',(e) => {
            // unlock();
            managerMoveMove = false;
            handleMouseUp(e);
        });
    } )
}
run();




function handleMouseUpCollect(data) {
    parentOpen.style.transitionDuration  = "0.6s";
    const getExtra = thumbMove__hidden.offsetTop % thumbMove__hidden.offsetHeight;
    let percent = (getExtra / thumbMove__hidden.offsetHeight) * 100;
    if(percent < 0) {
        percent = percent * -1;
    }

    if(processAddPre === true) {
        if(percent > 50) {
            parentOpen.style.transform = "translate3d(0px," + 0 + "px , 0px)";
            setTimeout(() => {
                resetEnd();
            }, (0.8 * 1000));
        }else  {
            parentOpen.style.transform = "translate3d(0px,-" + (heightInitThumb * getPercentUneven()) + "px, 0px)";
            setTimeout(() => {
                resetBegin();
            }, (0.8 * 1000));
        }
    }else
    if(processAddNext === true) {
        if(percent > 50) {
            parentOpen.style.transform = "translate3d(0px,-" + spaceMax * getPercentUneven() + "px , 0px)";
            setTimeout(() => {
                resetBeginNext();
            }, (0.8 * 1000));
        }else  {
            const dataPre = (spaceMax * getPercentUneven()) - 600;
            parentOpen.style.transform = "translate3d(0px,-" + dataPre+ "px , 0px)";
            setTimeout(() => {
                resetEndNext()
            }, (0.8 * 1000));
        }
    }
    else  {
        if(data < 0) {
            if(percent > 50) {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px,-" + (initData + dataSet)  +"px , 0px)";
            }else  {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px, -" + (initData - dataSet)  +"px , 0px)";
            }
            if(allowMoveNextThumb === false) {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px, -" + (initData - dataSet)  +"px , 0px)";
            }
        }else  {
            if(percent < 50) {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px, -" + (initData - dataSet)  +"px , 0px)";
            }else  {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px, -" + (initData + dataSet)  +"px , 0px)";
            }
            if(allowMovePreThumb === false) {
                const initData = (thumbMove__hidden.offsetTop * getPercentUneven());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentUneven()))
                parentOpen.style.transform = "translate3d(0px, -" + (initData + dataSet)  +"px , 0px)";
            }
        }
    }
    parentOpen.style.transitionDuration  = "0.6s";

}




function handleMouseUpCollectMobile(data) {
    parentOpen.style.transitionDuration  = "0.6s";
    const getExtra = thumbMove__hidden.offsetLeft % thumbMove__hidden.offsetWidth;
    let percent = (getExtra / thumbMove__hidden.offsetWidth) * 100;
    if(percent < 0) {
        percent = percent * -1;
    }

    if(processAddPre === true) {
        if(percent > 5) {
            parentOpen.style.transform = "translate3d(0px," + 0 + "px , 0px)";
            setTimeout(() => {
                resetEndMobile();
            }, (0.8 * 1000));
        } else  {
            parentOpen.style.transform = "translate3d(-" + (heightInitThumb * getPercentMobile()) + "px, 0px , 0px)";
            setTimeout(() => {
                resetBegin();
            }, (0.8 * 1000));
        }
    }else if(processAddNext === true) {
        if(percent > 5) {
            parentOpen.style.transform = "translate3d(-" + spaceMax * getPercentMobile() + "px, 0px , 0px)";
            setTimeout(() => {
                resetBeginNext();
            }, (0.8 * 1000));
        }else  {
            const dataPre = (spaceMax * getPercentMobile()) - parentThumbMoveOpen.offsetWidth;
            parentOpen.style.transform = "translate3d(-" + dataPre+ "px , 0px, 0px)";
            setTimeout(() => {
                resetEndNextMobile();
            }, (0.8 * 1000));
        }
    } else  {
        if(data < 0) {
            if(percent > 5) {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentMobile()))
                parentOpen.style.transform = "translate3d( -" + (initData + dataSet)  +"px, 0px , 0px)";
            }else  {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentMobile()))
                parentOpen.style.transform = "translate3d( -" + (initData - dataSet)  +"px, 0px , 0px)";
            }
            if(allowMoveNextThumb === false) {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentMobile()))
                parentOpen.style.transform = "translate3d( -" + (initData - dataSet)  +"px, 0px , 0px)";
            }
        } else  {
            if(percent < 95) {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (( percent/ 100) * (heightInitThumb * getPercentMobile()))
                let datUse = (initData - dataSet);
                if( datUse < 0) {
                    datUse = 0;
                }
                parentOpen.style.transform = "translate3d( -" + datUse +"px, 0px , 0px)";
            }else  {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentMobile()))
                parentOpen.style.transform = "translate3d( -" + (initData + dataSet)  +"px, 0px , 0px)";
            }
            if(allowMovePreThumb === false) {
                const initData = (thumbMove__hidden.offsetLeft * getPercentMobile());
                const dataSet = (((100 - percent)/ 100) * (heightInitThumb * getPercentMobile()))
                parentOpen.style.transform = "translate3d(-" + (initData + dataSet)  +"px, 0px , 0px)";
            }
        }
    }
    parentOpen.style.transitionDuration  = "0.6s";

}






function handleMouseUpThumb(data) {
    const getExtra = thumbMove__hidden.offsetTop % thumbMove__hidden.offsetHeight;
    let percent = (getExtra / thumbMove__hidden.offsetHeight) * 100;
}


function resetBeginNext() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((0).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) > 0) {
            data.push(e);
        }
    });
     data.unshift(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddNext = false;
    indexSetNext = 0;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d(0px, -" + 0  +"px , 0px)";
    parentOpen.style.transitionDuration  = "0s";
}

function resetBegin() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((array.length - 1).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) < array.length - 1) {
            data.push(e);
        }
    });
    data.push(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddPre = false;
    indexSetPre = listSlide.length - 1;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d(0px, -" + 0  +"px , 0px)";
    parentOpen.style.transitionDuration  = "0s";
}



function resetEndNext() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((array.length - 1).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) < array.length -1) {
            data.unshift(e);
        }
    });
    data.push(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddNext = false;
    indexSetNext =  0;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d(0px, -" + (spaceMax * getPercentUneven())  +"px , 0px)";
}

function resetEndNextMobile() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((array.length - 1).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) < array.length -1) {
            data.unshift(e);
        }
    });
    data.push(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddNext = false;
    indexSetNext =  0;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d(-" + (spaceMax * getPercentMobile())  +"px, 0px , 0px)";
}
function resetEnd() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((array.length - 1).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) < array.length -1) {
            data.push(e);
        }
    });
    data.push(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddPre = false;
    indexSetPre =  listSlide.length - 1;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d(0px, -" + (spaceMax * getPercentUneven())  +"px , 0px)";
}


function resetEndMobile() {
    let data = [];
    const  array = document.querySelectorAll(".slide-open__current > div");
    const  itemIndex = document.querySelector("#" + CSS.escape((array.length - 1).toString()));
    array.forEach((e) => {
        if(Number(e.getAttribute('data-index')) < array.length -1) {
            data.push(e);
        }
    });
    data.push(itemIndex);
    const  Slide = document.querySelector(".slide-open__current");
    data.forEach((e) => {
        Slide.append(e);
    });
    processAddPre = false;
    indexSetPre =  listSlide.length - 1;
    parentOpen.style.transitionDuration  = "0s";
    parentOpen.style.transform = "translate3d( -" + (spaceMax * getPercentMobile())  +"px, 0px , 0px)";
}


function handleMouseUpThumbHiden(data) {
    const getExtra = thumbMove__hidden.offsetTop % thumbMove__hidden.offsetHeight;
    let percent = (getExtra / thumbMove__hidden.offsetHeight) * 100;
    if(percent < 0) {
        percent = percent * -1;
    }

    if(processAddPre === true) {
        if(percent > 50) {
            thumbMove__hidden.style.top = spaceMax + "px";
        } else  {
            thumbMove__hidden.style.top = 0 + "px";
        }
    }else if(processAddNext === true) {
        if(percent > 50) {
            thumbMove__hidden.style.top = 0 + "px";
        }else  {
            thumbMove__hidden.style.top = spaceMax + "px";
        }
        if(allowMoveNextThumb === false) {
            thumbMove__hidden.style.top = spaceMax + "px";
        }
    } else  {

        if(data < 0) {
            if(percent > 50) {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop + dataAdd) + "px";
            }else  {
                const dataAdd = ( percent / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop - dataAdd) + "px";
            }
            if(allowMoveNextThumb === false) {
                const dataAdd = ( percent / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop - dataAdd) + "px";
            }
        } else  {
            if(percent < 50) {
                const dataAdd = (  percent / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop - dataAdd) + "px";
            }else  {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop + dataAdd) + "px";
            }
            if(allowMovePreThumb === false) {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.top = (thumbMove__hidden.offsetTop + dataAdd) + "px";
            }
        }
    }

    allowMoveNextThumb = null;
    allowMovePreThumb = null;

}


function handleMouseUpThumbHidenMobile(data) {
    parentOpen.style.transitionDuration  = "0.6s";
    const getExtra = thumbMove__hidden.offsetLeft % thumbMove__hidden.offsetWidth;
    let percent = (getExtra / thumbMove__hidden.offsetWidth) * 100;
    if(percent < 0) {
        percent = percent * -1;
    }

    if(processAddPre === true) {
        if(percent > 5) {
            thumbMove__hidden.style.left = spaceMax + "px";
        } else  {
            thumbMove__hidden.style.left = 0 + "px";
        }
    }else if(processAddNext === true) {
        if(percent > 5) {
            thumbMove__hidden.style.left = 0 + "px";
        }else  {
            thumbMove__hidden.style.left = spaceMax + "px";
        }
        if(allowMoveNextThumb === false) {
            thumbMove__hidden.style.left = spaceMax + "px";
        }
    } else  {

        if(data < 0) {
            if(percent > 5) {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft + dataAdd) + "px";
            }else  {
                const dataAdd = ( percent / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft - dataAdd) + "px";
            }
            if(allowMoveNextThumb === false) {
                const dataAdd = ( percent / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft - dataAdd) + "px";
            }
        } else  {
            if(percent < 95) {
                const dataAdd = (  percent / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft - dataAdd) + "px";
            }else  {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft + dataAdd) + "px";
            }
            if(allowMovePreThumb === false) {
                const dataAdd = ( (100 - percent) / 100) * heightInitThumb;
                thumbMove__hidden.style.left = (thumbMove__hidden.offsetLeft + dataAdd) + "px";
            }
        }
    }

    allowMoveNextThumb = null;
    allowMovePreThumb = null;

}
































function  setUpAmount() {
    const input = document.querySelector(".amount__input");
    const  decreaseIcon = document.querySelector(".fa-minus");
    const  increaseIcon = document.querySelector(".fa-plus");
    if(getCheckItemSize() != null) {
        const value  = getCheckItemSize().getAttribute('data-quantity');
        if(Number(input.value) >= Number(value)) {
            input.value = Number(value);
            increaseIcon.classList.add("disable");
            if(decreaseIcon.getAttribute("class").includes("disable")) {
                decreaseIcon.classList.remove("disable");
            }
        }else if(Number(input.value) <= 1) {
            input.value = 1;
            decreaseIcon.classList.add("disable");
        }else  {
            if(increaseIcon.getAttribute("class").includes("disable")) {
                increaseIcon.classList.remove("disable");
            }
            if(decreaseIcon.getAttribute("class").includes("disable")) {
                decreaseIcon.classList.remove("disable");
            }
        }
    }else  {
        if(Number(input.value) < 1) {
            input.value = 1;
        }else
        if(Number(input.value) === 1) {
            decreaseIcon.classList.add("disable");
        }else if(Number(input.value) >= 10) {
            increaseIcon.classList.add("disable");
        } else {
            if(increaseIcon.getAttribute("class").includes("disable")) {
                increaseIcon.classList.remove("disable");
            }

            if(decreaseIcon.getAttribute("class").includes("disable")) {
                decreaseIcon.classList.remove("disable");
            }
        }
    }
}


const  handleIncrease = () => {
    const input = document.querySelector(".amount__input");
    if(getCheckItemSize() === null) {
        if(Number(input.value) < 10) {
            input.value = Number(input.value) + 1;
            setUpAmount();
        }
    } else  {
        input.value = Number(input.value) + 1;
        setUpAmount();
    }
}

const  handleDecrease = () => {

    const input = document.querySelector(".amount__input");
    if(Number(input.value) > 1) {
        input.value = Number(input.value) - 1;
        setUpAmount()
    }
}




$(window).ready(function() {
    // const dataColors = document.querySelectorAll(".color__item--display");
    // dataColors.forEach((item) => {
    //     let color = item.getAttribute("data-color").trim();
    //     let name = item.getAttribute("data-name").trim().replaceAll(" ","-");
    //     let url = "/products/" + name + "-" + color;
    //     item.setAttribute("href", url);
    // })
});

function getCheckItemSize() {
    let  data = null;
    const arrays = document.querySelectorAll(".size__item");
    arrays.forEach((e) => {
        if(e.getAttribute('class').includes('active-size')) {
            data = e;
        }
    });
    return data;
}

function changeData(e)  {
    const value = e.target.value;
    const dataCheckSize = getCheckItemSize();
    if(dataCheckSize  === null) {
        e.target.value = 1;
    } else  {
        const dataQuantity = dataCheckSize.getAttribute('data-quantity');
        if(Number(value) > Number(dataQuantity)) {
            e.target.value = Number(dataQuantity);
        }
        if(Number(value)  < 1) {
            e.target.value = 1;
        }
    }
    setUpAmount();
}



function copyLink() {
    const name = document.querySelector(".specific__name").innerText;
    const price = document.querySelector(".specific__price:nth-child(1)").innerText;
    // const price2 = document.querySelector(".specific__price:nth-child(2)");
    const img = document.querySelector(".slide-open__slide--item > img").getAttribute('src');
    const html = "        <div class=\"card__copy\">\n" +
        "              <img src='"+ img +"'>\n" +
        "              <div class=\"card__copy--content\">\n" +
        "                  <span class=\"content__success\">\n" +
        "                      <span>Sao chép liên kết thành công </span>\n" +
        "                      <i class=\"fa-solid fa-check\"></i>\n" +
        "                  </span>\n" +
        "                  <span>"+ name +"</span>\n" +
        "                  <span>" + price + "</span>\n" +
        "              </div>\n" +
        "          </div>"
    const content = document.querySelector('.error__content');
    content.innerHTML = html;
    const  url = window.location.href;
    navigator.clipboard.writeText(url.toString());
    lockA();
    const  error = document.querySelector(".error");
    error.classList.add("active_error");

}