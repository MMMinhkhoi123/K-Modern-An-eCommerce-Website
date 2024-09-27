function apiSetStateRating(data) {
    axios.post("/admin/rating/update", data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    }).then((e) => {
        $('.customer_frame').html(e.data);
    })
}