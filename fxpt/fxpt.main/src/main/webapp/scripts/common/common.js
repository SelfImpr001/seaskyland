function getByClass (sClass,parent ){
     var aEles=parent.getElementsByTagName('*');
     var arr=[];
    for(var i=0; i<aEles.length; i++){
        var aClass=aEles[i].className.split(' ');   //一个元素可能有几个class
        for(var j=0; j<aClass.length; j++){
            if( aClass[j]===sClass){
                arr.push(aEles[i]);
            }
         }
    }
    return arr;
}



function scrollTop(){
	return document.body.scrollTop || document.documentElement.scrollTop;

}

function getTop(obj){
   var iTop=0;
   while(obj){
      iTop+=obj.offsetTop;
     
      obj=obj.offsetParent;
   }
   return iTop;
}


function startMove(obj,json,endFn){
 
  clearInterval(obj.timer);
 
  obj.timer = setInterval(function(){ 

    

   var bBtn = true; 
    
   for(var attr in json){ 
     
    var iCur = 0; 
    
    if(attr == 'opacity'){ 
     if(Math.round(parseFloat(getStyle(obj,attr))*100)==0){ 
     iCur = Math.round(parseFloat(getStyle(obj,attr))*100); 
      
     } 
     else{ 
      iCur = Math.round(parseFloat(getStyle(obj,attr))*100) || 100; 
     }  
    }  else{ 
     iCur = parseInt(getStyle(obj,attr)) || 0; 
    } 
     
    var iSpeed = (json[attr] - iCur)/8; 
   iSpeed = iSpeed >0 ? Math.ceil(iSpeed) : Math.floor(iSpeed); 
    if(iCur!=json[attr]){ 
     bBtn = false; 
    } 
     
    if(attr == 'opacity'){ 
     obj.style.filter = 'alpha(opacity=' +(iCur + iSpeed)+ ')'; 
     obj.style.opacity = (iCur + iSpeed)/100; 
      
    } 
    else{ 
     obj.style[attr] = iCur + iSpeed + 'px'; 
    } 
     
     
   } 
    
   if(bBtn){ 
    clearInterval(obj.timer); 
     
    if(endFn){ 
     endFn.call(obj); 
    } 
   } 
    

  },30);
 
 }
function getStyle(obj,attr){
	if(obj.currentStyle){
		return parseInt( obj.currentStyle[attr] );
	}
	return parseInt( getComputedStyle(obj)[attr] );
}

function getByClass(oParent,oClass){
	var arr=[];
	var aElems=oParent.getElementsByTagName('*');
	for(var i=0; i<aElems.length; i++){
		var arr1=aElems[i].className.split(' ');
		for(var j=0; j<arr1.length; j++){
			if(arr1[j]==oClass){
				arr.push(aElems[i]);
			}
		}
	}
	return arr;
}
/*  元素拖动效果  onmouseover="comDrag(this)"  */
function comDrag(obj){
	var parent = obj.parentNode;
	var disX=0, disY=0;
	obj.onmousedown=function(ev){
		if(ev && ev.button != 0) return;
		var ev = ev || window.event;
		disX=ev.clientX-parent.offsetLeft;
		disY=ev.clientY-parent.offsetTop;
		bind(document,'mousemove',fnMove);
		obj.style.cursor = "move";
		function fnMove(ev){
			var ev= ev || event;
			var L=ev.clientX-disX;
			var T=ev.clientY-disY;
			parent.style.left=L+'px';
			parent.style.top=T+'px';
		};
		
		bind(document,'mouseup',fnUp);
		function fnUp(){
			obj.style.cursor = "default";
			remove(document,'mousemove',fnMove);
			remove(document,'mouseup',fnUp);
		};
		
		function bind(obj, evName, fn) {
			if (obj.addEventListener) {
				obj.addEventListener(evName, fn, false);
			} else {
				obj.attachEvent('on' + evName, fn);
			}
		};
		
		function remove(obj, evName, fn){
			if( obj.removeEventListener){
				obj.removeEventListener(evName,fn,false);
			}else{
				obj.detachEvent('on'+evName, fn);	
			}		
		}
		
		return false;
	};
}
















