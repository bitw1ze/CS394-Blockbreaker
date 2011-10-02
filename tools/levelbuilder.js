var path = "../res/drawable/";
var width = 480;
var height = 800;
var rows = 0;
var cols = 0;
var blank = "blank.png";
var blocks = new Array("block.png", "block2.png", "block3.png", "block4.png", "block5.png");
var MAX_ALPHA = 2;
var BASE_ALPHA = .3;
var edit_mode = 0;
var paint = new Image();
var paint_index = 0;
var paints = new Array();
var grid = new Array();
var alphas = new Array();

function indexOf(p, parr) {
	for (i=0; i<parr.length; i++) {
		if (p == parr[i]) {
			return i;		
		}
	}
	return -1;
}

/*
function reset_blocks(elems) {
	img_grid = elems;
	for (i=0; i<rows; i++) {
		for (j=0; j<cols; j++) {
			grid[i][j] = blocks.length;
			img_grid[i][j].src = blocks[0];
		}
	}
}
*/

function init_panel() {
	var panel = document.getElementById("panel");
	panel.innerHTML = "<img src='" + path + blank + "' onclick='set_paint(this)' name='paints' />";
	for (i=0; i<blocks.length; i++) {
		panel.innerHTML += ("<img src='" + path + blocks[i] + "' onclick='set_paint(this)' name='paints' />");
	}
	paints = document.getElementsByName("paints");
	set_paint(paints[0]);
}

function set_paint(elem) {
	for (i=0; i<paints.length; i++) {
		paints[i].className = "";
	}
	elem.className = "selected";
	paint.src = elem.src;
	paint_index = indexOf(elem, paints) - 1;
}

function edit_block(elem) {
	pos = parseInt(elem.id);
	r = Math.floor(pos / cols);
	c = pos % cols;
	
	if (edit_mode == 0) {
		grid[r][c] = paint_index;
		elem.src = path + blocks[paint_index];
	}
	else {
		alphas[r][c] = (alphas[r][c] + 1) % MAX_ALPHA;
		elem.style.opacity =  BASE_ALPHA + (alphas[r][c] * (1 - BASE_ALPHA));
	}
}

function init_blocks(r, c) {
	level = document.getElementById("level");
	rows = r.value;
	cols = c.value;
	level.innerHTML = "";
	var block = blocks[0];
	
	k = 0;
	for (i=0; i<rows; i++) {
		grid[i] = new Array();
		alphas[i] = new Array();
		for (j=0; j<cols; j++, k++) {
			grid[i][j] = -1;
			alphas[i][j] = MAX_ALPHA - 1;
			level.innerHTML += ("<img id='" + k + "'" +
			" onclick='edit_block(this)' width=" + Math.floor(width/cols) + 
			" height=" + Math.floor(height/rows) + " src='" + path + blank + "' />");
		}
		
		level.innerHTML += "<br />";
	}
}

function save() {
	var out = "public static int " + document.getElementById("name").innerHTML + "[][] = {\n";
	
	for (i=0; i<rows; i++) {
		out += "{";
		for (j=0; j<cols; j++) {
			if (grid[i][j] == -1) {
				out += -1;
			}
			else {
				out += (grid[i][j] + (alphas[i][j] * blocks.length));
			}
			
			if (j != cols - 1) {
				out += ",";
			}
		}
		
		out += "}";
		
		if (i != rows - 1)
		{
			out += ",\n";
		}
	}
	
	out += "\n};";
	
	alert(out);
			
}