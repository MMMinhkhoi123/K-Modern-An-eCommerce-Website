function apiSearch(key, state) {
    axios.get('/admin/customer/search?q='+ key + "&state=" + state, {}).then((e) => {
        $('.parent__color-section--list').html(e.data);
    })
}

function apiGetState(key) {
    axios.get('/admin/customer/state/'+ key, {}).then((e) => {
        $('.customer_frame').html(e.data);
    })
}