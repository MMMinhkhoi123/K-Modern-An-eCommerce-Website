function actionRating(state, id) {
    const data = new FormData();
    data.set("idRating", id);
    data.set("state", state)
    apiSetStateRating(data);
}