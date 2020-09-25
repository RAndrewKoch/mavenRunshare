function displayMilesFromHome (homeZip, trailZip){
//Using mapquestapi for distance between zipcodes
    console.log ('displayMilesFromHome fired');
    let distanceFromHomeSpan = document.getElementById
    ("distanceFromHomeSpan");
    let distanceButton = document.getElementById
    ("distanceButton");
    let distanceSpan = document.getElementById
    ("distanceSpan");
    homeZip=getZipFromNumberZip(homeZip);
    trailZip=getZipFromNumberZip(trailZip);

//    console.log("https://www.mapquestapi.com/directions/v2/route?key=4SyQnpPVIGhLOWWcbkwdt0iEh9HkBxtb&from="+homeZip+"&to="+trailZip+"&outFormat=json'");
    const response = fetch("https://www.mapquestapi.com/directions/v2/route?key="+callDistanceApiKey()+"&from="+homeZip+"&to="+trailZip+"&outFormat=json'");
    response.then(function(response){
        if (response.status === 404){
            alert(`Sorry, we cannot seem to get the distance between these locations, please check the respective zip codes`);
        } else {
        const jsonPromise = response.json();
        jsonPromise.then(function(json){
            if (json.route.distance == null){
                alert(`Distance between runner home and
                trail location cannot be determined.
                Likely invalid zip codes`);
                distanceButton.remove();
                distanceFromHomeSpan.remove();
                distanceSpan.remove();
            } else {
            distanceButton.setAttribute("hidden", "");
            distanceFromHomeSpan.removeAttribute("hidden");
            console.log(json);
            distanceFromHomeSpan.innerHTML = "Distance from home : "+json.route.distance+" miles";
            }
            })
        }
    });
}

function getZipFromNumberZip (zip){
    let zipString = zip.toString();
    while (zipString.length<5){
        zipString = "0"+zipString;
    }
    return zipString
}