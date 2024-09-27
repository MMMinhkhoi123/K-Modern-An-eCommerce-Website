function  apiFilter(value) {
    axios.get('/admin/dashboard/filter-overview/' + value,{})
        .then((e) => {
            $('.admin__screen-frame').html(e.data);
            setUpMoney();
        })
}