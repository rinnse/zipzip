$(document).ready(function(){


	var change;
	//console.log('처음부터?');
    $("input[type='checkbox']").change(function() {
        checkBox();
        if(change){
    		filterList();
    	}
    	else{
    		applyFiltering();
    	}
    });
    
    $('input[type="radio"][name="pType"]').change(function() {
    	console.log(change);
        if(change){
        	console.log("hah");
    		filterList();
    	}
    	else{
    		console.log("nah");
    		applyFiltering();
    	}
    });
    $('input[type="range"]').change(function(){
        if(change){
    		filterList();
    	}
    	else{
    		applyFiltering();
    	}
    });

});
function applyFiltering() {
    change = false;
    //console.log(change);
    //alert('applyFiltering...');
    var encludedPType;
    var basis = [];
    
    $('.menu-checkbox[name="searchSize"]:checked').each(function(){
        var size = parseInt($(this).attr('id').replace('chk', ''));
        if (size == "All"){
            basis.push(0);  
        } else {
            basis.push(size);
        }
        //console.log(size);
    });
    
    $('input[name="pType"]:checked').each(function(){
        var pType = $(this).val();
        if (pType) {
            encludedPType = pType;
        }
        console.log(pType);
    });
    
    var selectedPrice = parseInt($('input[type="range"]').val());
    var realPrice = 0;
    console.log('s:' + selectedPrice);
    if(selectedPrice < 100){
        if(selectedPrice <= 50){
            realPrice = selectedPrice * 200;
        } else {
            realPrice = selectedPrice * 2000;
        }
        //console.log('r:' + realPrice);
    }

    
    $('.oneItem').each(function(index){
        var size = parseInt($(this).find('.item-info span:first-child').text());
	    var pType = $(this).find(".PayType").text(); // 현재 요소 내에서만 찾도록 수정
	    var price = $(this).find(".transD").text().replace('억','0000'); // 현재 요소 내에서만 찾도록 수정
	    price = parseInt(price);
	    if(selectedPrice == 100){
	        realPrice = price;
	    }
	    console.log("size: "+size+", price: "+price+", pType: "+pType);
	    //alert(price);
	    //alert(size);
	    //alert(price+":"+realPrice);
	    console.log(index);
	    if(chkSize(basis, size) == false || (encludedPType!="전체" && encludedPType != pType) 
	                            || price>realPrice && realPrice != 0){
	        $(this).hide();
	    }
	    else{
	        $(this).show();
	    }

    });
};
function chkSize (basis, size){
	var see = false;
    if (basis.includes(0)){
        console.log('basis.includes(0)');
        see = true;
    }
    if (basis.includes(33)){
        if(size <= 65){
            console.log('size <= 65');
            see = true;
        }
    }
    if(basis.includes(66)){
        if(66 <= size && size <= 98){
            console.log('66 <= size <= 98');
            see = true;
        }
    }
    if(basis.includes(99)){
        if(99 <= size && size <= 131){
            console.log('99 <= size <= 131');
            see = true;
        }
    }
    if(basis.includes(132)){
        if(132 <= size){
            console.log('132 <= size');
            see = true;
        }
    }
    console.log("see: "+see);
    return see;
};
function filterList() {
    var encludedPType;
    var basis = [];
    
    $('.menu-checkbox[name="searchSize"]:checked').each(function(){
        var size = parseInt($(this).attr('id').replace('chk', ''));
        if (size == "All"){
        	basis.push(0);	
        }
        else{
        	basis.push(size);
        }

    });
    
    $('input[name="pType"]:checked').each(function(){
        var pType = $(this).val();
        if (pType) {
            encludedPType = pType;
        }
    });
    
    var selectedPrice = parseInt($('input[type="range"]').val());
    var realPrice = 0;
    console.log('s:'+selectedPrice);
    if(selectedPrice < 100){
        if(selectedPrice<=50){
        	realPrice = selectedPrice*200;
        }
        else{
        	realPrice = selectedPrice * 2000;
        }
        console.log('r:'+realPrice);
    }

    
    // 각 매물을 순회하면서 필터링합니다.
    $('.oneItem').each(function(){
        var size = parseInt($(this).find('.item-info span:first-child').text());
        var pType = $("#PayType").text();
        var price = $("#transD").text().replace('억','0000');
        price = parseInt(price);
        if(selectedPrice == 100){
        	realPrice = price;
        }
        //alert(price);
        //alert(size);
        //alert(price+":"+realPrice);
       	if( chkSize(basis, size) == false|| encludedPType!="전체" || encludedPType != pType || price>realPrice && realPrice != 0){
       		$(this).hide();
       	}
       	else{
       		$(this).show();
       	}

    });
}
function openFilterModal() {
    var modal = $("#filterModal");
    if (modal.css("display") === "none") {
      modal.css("display", "block");
      initChk();
    } else {
      modal.css("display", "none");
    }
};
function initChk(){ 
	$("input[type='checkbox']").prop("checked", true);
	$('input[name="pType"][value="전체"]').prop('checked', true);
	$("input[type='range']").val(100);
	//filterList();
	if(change){
    		filterList();
    	}
    	else{
    		applyFiltering();
    	}
};
//체크박스가 변했을때
function checkBox(){
	if($("#chkAll").is(":checked")){
		$("input[type='checkbox']").prop("checked", true);
          } 
	if($("#chk33").is(":checked")){
		$("#chkAll").prop("checked", false);
	}
	if($("#chk66").is(":checked")){
		$("#chkAll").prop("checked", false);	
						}
	if($("#chk99").is(":checked")){
		$("#chkAll").prop("checked", false);
	}
	if($("#chk132").is(":checked")){
		$("#chkAll").prop("checked", false);
	}
};
function openIframe(itemNum, type) {
	//alert("itemNum: "+itemNum);
	console.log("오픈아이프레임 "+itemNum);
    var iframeContainer = document.getElementById("iframeContainer");
    var propertyIframe = document.getElementById("propertyIframe");
    var originList = document.getElementById("itemBox");
    document.getElementById("iframeContainer").style.display = "none";		//아이프레임 숨기기
    // 	document.getElementById("rightSidebarContent").style.display = "block";
    propertyIframe.src = "/detail?itemNum="+itemNum+"&type="+type; 
}
function fn_showIframe(itemNum, type) {
//  alert(itemNum);
 
 var iframeContainer = document.getElementById("iframeContainer");
 
 document.getElementById("propertyIframe").src = "/detail?itemNum="+itemNum+"&type="+type;      //propertyIframe
 iframeContainer.style.display = "block";                                         // 아이프레임 표시
 document.getElementById("searchBar").style.display = "none";
 document.getElementById("rightSidebarContent").style.display = "none";
}