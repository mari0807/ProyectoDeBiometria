var fs = require("fs");
var path = require("path");
var vm = require("vm");

var fallos = 0;
var getUrl = null;

global.fetch = function (url, options) {
    options = options || {};
    getUrl = url;
    return Promise.resolve({
        ok: true,
        json: function () {
            return Promise.resolve({
                success: true,
                medicion: { id_medicion: 7, minor: 123 }
            });
        }
    });
};

vm.runInThisContext(fs.readFileSync(path.join(__dirname, "LogicaProxy.js"), "utf8"));

var laLogica = new Logica();

laLogica.obtenerMedicion(function (err, medicion) {
    if (err || !medicion || medicion.minor !== 123 || !getUrl || getUrl.indexOf("ReglasREST.php") === -1) {
        fallos++;
        console.log("Fallo obtenerMedicion");
    } else {
        console.log("obtenerMedicion minor=" + medicion.minor);
    }
    if (fallos > 0) {
        console.log("Pruebas de proxy: " + fallos + " fallos");
        process.exit(1);
    }
    console.log("Pruebas de proxy: ok");
    process.exit(0);
});
