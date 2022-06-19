const App = require("./app");

const Data = require("./data/data");
const View = require("./views/view");
const view = new View(new Data());
const app = new App(view).app;

var server = app.listen(8000, function() {
  var host = server.address().address;
  var port = server.address().port;
  var family = server.address().family;
  
  console.log(`Aapp listening (${family}) at http://${host}:${port}`);
});
