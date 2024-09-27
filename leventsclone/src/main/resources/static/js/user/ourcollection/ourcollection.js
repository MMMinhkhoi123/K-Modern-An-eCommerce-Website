$(window).ready(() => {
    const  array = document.querySelectorAll(".item__content--link > a");
    array.forEach((item) => {
        const  convertUrl = item.getAttribute("data-url").replaceAll(" ", "-");
        const  convertUrlLevel2 = convertUrl.replaceAll("/","-");
        item.setAttribute("href" , '/collections/' +  convertUrlLevel2);
    })
})

function hoverItem(thisElement) {
    var style = document.createElement('style');
    style.innerText = '.item__content--link:hover > a:after{\n' +
        '    position: absolute;\n' +
        '    inset: -1px;\n' +
        '    display: flex;\n' +
        '    justify-content: center;\n' +
        '    background: '+ thisElement.getAttribute('data-color') +';\n' +
        '    align-items: center;\n' +
        '    content: "Xem bộ sưu tập";\n' +
        '}'; // Modify the height as needed
    document.body.appendChild(style);
}