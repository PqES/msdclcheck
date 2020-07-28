module.exports = function(a){
	module.exports.falar = function falar(){
		return a;
	}
	this.c = a;
	this.b = 'tchau';
	return this;
}
