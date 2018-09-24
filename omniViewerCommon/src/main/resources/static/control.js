/*
 * function library
 */

function setFilter(filter) {
    var el = document.getElementById("rawFilter");
    el.value = filter;
    document.getElementById("formQuery").submit();
}
function changeColor(name, state) {
    var tr = document.getElementById('id' + name);
    var color = state ? "#d8bfd8;" : "inherit;"; //transparent
    var bgcolor = "background-color: " + color;
    tr.setAttribute("style", bgcolor);
}
function setAll(state) {
    var els = document.querySelectorAll('input[type=checkbox]');
    for (var i = 0; i < els.length; i++) {
        if (els[i].name.toLowerCase() !== "ack") {
            els[i].checked = state;
            changeColor(els[i].value, state);
        }
    }
}
function advField(state) {
    var els = document.getElementsByClassName('adv-hidden');
    for (var i = 0; i < els.length; i++)
        els[i].hidden = !state;
    if (document.getElementById('view').value === "true") {
        els = document.getElementsByClassName('adv-input');
        for (var i = 0; i < els.length; i++)
            els[i].readOnly = !state;
    }
}
function generateId() {
    if (document.getElementById('genid').checked) {
        var agent = document.getElementById('agent').value;
        var akey = document.getElementById('akey').value;
        var agroup = document.getElementById('agroup').value;
        var node = document.getElementById('node').value;
        var type = document.getElementById('type').value;
        document.getElementById('id').value = akey + ':'
                + node + ':' + agroup + ':' + agent + ':' + type;
    }
}