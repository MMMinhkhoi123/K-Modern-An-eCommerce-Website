function  apiGetFilter(data) {
    axios.post('/admin/report/filter',data,{
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    })
        .then((e) => {
            $('.admin__screen-full').html(e.data);
            setUpMoney();
        })
}