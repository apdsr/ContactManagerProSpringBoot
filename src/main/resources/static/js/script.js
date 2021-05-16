console.log("This is script file");

const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")) {

        $(".sidebar").css("display", "none")
        $(".content").css("margin-left", "0%")
    } else {
        $(".sidebar").css("display", "block")
        $(".content").css("margin-left", "20%")
    }
}

const searchContact=()=>{
	
	//console.log("calling....")
	
	let query=$("#search-input").val();
	console.log(query);
	
	if(query==''){
		
		$(".search-result").hide();
	}
	else{
		
		//sending request to backend
		
		let url=`http://localhost:8081/search/${query}`;
		
		fetch(url).then((response)=>{
			return response.json();
			
		}).then((data)=>{
			
			console.log(data);
			
			let text=`<div class="list-group">`;
			
			data.forEach((contact)=>{
				text+=`<a href="/user/${contact.contactId}/contact" class="list-group-item list-group-item-action">${contact.contactName+' '+contact.contactSecondname}</a>`;
			});
			
			
			text+=`</div>`;
			
			$(".search-result").html(text);
		    $(".search-result").show();
		});
		
	}
	
}
