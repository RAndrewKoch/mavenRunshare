

function allowDrop(ev){
    ev.preventDefault();
}

function resizeImageTo100Width(originalImage){
    var ratio = originalImage.height/originalImage.width;
    originalImage.width = 100;
    originalImage.height = 100*ratio;
    var resizingCanvas = document.createElement('canvas');
    resizingCanvas.width = originalImage.width;
    resizingCanvas.height = originalImage.height;
    var ctx = resizingCanvas.getContext('2d');
    ctx.drawImage(originalImage,0,0,originalImage.width,originalImage.height);
    var resizedImageDataUrl = resizingCanvas.toDataURL();
    console.log(resizedImageDataUrl);
    return resizedImageDataUrl;
}

function drop(ev){
    var files= ev.dataTransfer.files;
    var file = files[0];
    if (file.type!="image/jpeg"&&file.type!="image/png"&&file.type!="image/jpg"){
        var imageWarning= document.getElementById("JPGPNGWarning");
        imageWarning.removeAttribute('hidden');
    } else {
        var imageWarning= document.getElementById("JPGPNGWarning");
        imageWarning.setAttribute('hidden', true);
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function (){
            var originalImage = document.createElement('img');
            originalImage.src=reader.result;
            originalImage.onload=function(){
                var resized = resizeImageTo100Width(originalImage);
                var runnerPhoto = document.getElementById('runnerPhoto');
                runnerPhoto.value=resized;
                var droppedImage = document.getElementById('droppedImage');
                droppedImage.src=resized;
                droppedImage.onload=function(){
                setToPhotoSelected();
                }
            }
        }
    }
    ev.preventDefault();
}

window.onload = function (){
    var imageDrop = document.getElementById("imageDrop");
    imageDrop.ondragover = allowDrop;
    imageDrop.ondrop = drop;
    isPictureAlreadyLoaded();
}

function processEnteredFile(file){
    if (file.type!="image/jpg"&&file.type!="image/jpeg"&&file.type!="image/png"){
        console.log("wrong type");
        var imageWarning= document.getElementById("JPGPNGWarning");
        imageWarning.removeAttribute('hidden');
    } else {
        var imageWarning= document.getElementById("JPGPNGWarning");
        imageWarning.setAttribute('hidden', true);
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function (){
            var originalImage = document.createElement('img');
            originalImage.src=reader.result;
            originalImage.onload=function(){
                var resized = resizeImageTo100Width(originalImage);
                var runnerPhoto = document.getElementById('runnerPhoto');
                runnerPhoto.value=resized;
                setToPhotoSelected();
            }
        }
    }
}

function isPictureAlreadyLoaded(){
    if (document.getElementById('runnerPhoto').value!=""){
        setToPhotoSelected();
    }
}

function setToPhotoSelected (){
    var imageDrop = document.getElementById('imageDrop');
    imageDrop.setAttribute('hidden', true);
    var imageChooseBox = document.getElementById('imageChooseBox');
    imageChooseBox.setAttribute('hidden', true);
    var imageChooseBoxMessage = document.getElementById('imageChooseBoxMessage');
    imageChooseBoxMessage.setAttribute('hidden', true);
    var imageDropMessage = document.getElementById('imageDropMessage');
    imageDropMessage.setAttribute('hidden', true);
    var JPGPNGMessage = document.getElementById('JPGPNGMessage');
    JPGPNGMessage.setAttribute('hidden', true);
    var droppedImageBox = document.getElementById('droppedImageBox');
    droppedImageBox.removeAttribute('hidden');
    var successfulUpload = document.getElementById('successfulUpload');
    successfulUpload.removeAttribute('hidden');
    var resetImageMessage = document.getElementById('resetImageMessage');
    resetImageMessage.removeAttribute('hidden');
    var droppedImage = document.getElementById('droppedImage');
    var loadedPicture = document.getElementById('runnerPhoto').value
    droppedImage.src = loadedPicture;
}

function resetDroppedImage(){
var imageDrop = document.getElementById('imageDrop');
    imageDrop.removeAttribute('hidden');
    var imageChooseBox = document.getElementById('imageChooseBox');
    imageChooseBox.removeAttribute('hidden');
    var imageChooseBoxMessage = document.getElementById('imageChooseBoxMessage');
    imageChooseBoxMessage.removeAttribute('hidden');
    var imageDropMessage = document.getElementById('imageDropMessage');
    imageDropMessage.removeAttribute('hidden');
    var JPGPNGMessage = document.getElementById('JPGPNGMessage');
    JPGPNGMessage.removeAttribute('hidden');
    var droppedImageBox = document.getElementById('droppedImageBox');
    droppedImageBox.setAttribute('hidden', true);
    var successfulUpload = document.getElementById('successfulUpload');
    successfulUpload.setAttribute('hidden', true);
    var resetImageMessage = document.getElementById('resetImageMessage');
    resetImageMessage.setAttribute('hidden', true);
    var droppedImage = document.getElementById('droppedImage');
    droppedImage.src="";
    document.getElementById('runnerPhoto').value=null;
    document.getElementById('imageChoose').value=null;
}