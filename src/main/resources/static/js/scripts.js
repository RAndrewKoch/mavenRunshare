let navClosed = true;

function openNav(){
    if (navClosed===true){
      if (screen.width<800){
          document.getElementById("sideNav").style
          .width="100vw";
          document.getElementById("main").style
          .width="100vw";
          navClosed=false;
          } else {
          document.getElementById("sideNav").style.width ="25%";
          document.getElementById("main").style
          .width="75%";
          document.getElementById("main").style
          .marginLeft="25%";
          navClosed = false;
         }
    } else {
         closeNav();
    }
}

function closeNav() {
    document.getElementById("sideNav").style.width = "0";
    document.getElementById("main").style
    .marginLeft="0";
    document.getElementById("main").style.width="100%";
    navClosed= true;
}

function openMenu(menuId){
    let runnerMenu = document.getElementById(menuId);
    runnerMenu.style.transition = "all 1s";
    runnerMenu.removeAttribute("style");
}

function closeMenu(menuId){
    let runnerMenu = document.getElementById(menuId);
    runnerMenu.style.display = "none";
    ;
}

var scroll = window.requestAnimationFrame || function
(callback){ window.setTimeout(callback, 1000/60)};

var elementsToShow = document.querySelectorAll('.show-on-scroll');

function loop (){
    elementsToShow.forEach(function(element){
        if (isElementInViewport(element)){
            element.classList.add('is-visible');
        }  else {
            element.classList.remove('is-visible');
        }
    })
    scroll(loop);
}

function makeDisappear (element){
    element.style.display="none";
}

function toggleComments (){
    var topCommentsButtonSpan = document
    .getElementById
    ("topCommentsButtonSpan");
    var bottomCommentsButtonSpan = document.getElementById
    ("bottomCommentsButtonSpan");
    var comments = document.getElementById("comments");
    var bottomCommentsButton = document.getElementById
    ("bottomCommentsButton")
    if (comments.hasAttribute("hidden")){
        topCommentsButtonSpan.innerHTML="Hide";
        bottomCommentsButtonSpan.innerHTML="Hide";
        comments.removeAttribute("hidden");
        bottomCommentsButton.removeAttribute("hidden");
    } else if (!comments.hasAttribute("hidden")){
        topCommentsButtonSpan.innerHTML="Display";
        bottomCommentsButtonSpan.innerHTML="Display";
        comments.setAttribute("hidden", true);
        bottomCommentsButton.setAttribute("hidden", true);
    }
}

function toggleRunSessions (){
    var topRunSessionsButtonSpan = document .getElementById
    ("topRunSessionsButtonSpan");
    var bottomRunSessionsButtonSpan = document.getElementById("bottomRunSessionsButtonSpan");
    var runSessions = document.getElementById
    ("runSessionsList");
    var runSessionsJoined = document.getElementById
    ("runSessionsListJoined");
    var bottomRunSessionsButton = document.getElementById
    ("bottomRunSessionsButton")
    if (runSessions.hasAttribute("hidden")){
        topRunSessionsButtonSpan.innerHTML="Hide";
        bottomRunSessionsButtonSpan.innerHTML="Hide";
        runSessions.removeAttribute("hidden");
        runSessionsJoined.removeAttribute("hidden");
        bottomRunSessionsButton.removeAttribute("hidden");
    } else if (!runSessions.hasAttribute("hidden")){
        topRunSessionsButtonSpan.innerHTML="Display";
        bottomRunSessionsButtonSpan.innerHTML="Display";
        runSessions.setAttribute("hidden", true);
        runSessionsJoined.setAttribute("hidden", true);
        bottomRunSessionsButton.setAttribute("hidden", true);
    }
}
