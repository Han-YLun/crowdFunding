function showMenu() {
    var href = window.location.href;
    var ip = window.location.host;
    var indexOf = href.indexOf(ip);
    var path = href.substring(indexOf + ip.length);

    var contextPath =  "/";
    var pathAddress = path.substring(contextPath.length);

    var aLink = $(".list-group a[href*='"+pathAddress+"']");
    aLink.css("color","red");

    aLink.parent().parent().parent().removeClass("tree-closed");
    aLink.parent().parent().show();
}
