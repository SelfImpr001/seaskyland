function StringBuffer() {
	var content = new Array();

	this.append = function(str) {
		content.push(str);
	}

	this.toString = function() {
		return content.join("");
	}
}