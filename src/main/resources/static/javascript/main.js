function createRow() {
  let taskValue = document.getElementById("myInput").value;

  if (taskValue) {
    let dataToPost = {
      name: taskValue,
    };

    // Create via api
    fetch("http://localhost:8089/task/create/", {
      method: "post",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(dataToPost),
    })
      .then(function () {
        readAll();
        console.log("Create successful");
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });

    document.getElementById("myInput").value = "";
  } else {
    window.alert("Please enter a task");
  }
}

function deleteRow(r) {
  if (confirm("Are you sure you want to delete?")) {
    let id = r.parentNode.parentNode.childNodes[0].innerHTML;

    //delete via api

    fetch("http://localhost:8089/task/delete/" + id, {
      method: "delete",
      headers: {
        "Content-type": "application/json",
      },
    })
      .then(function () {
        readAll();
        console.log("Delete successful");
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });
  }
}

function updateRow(r) {
  let id = r.parentNode.parentNode.childNodes[0].innerHTML;
  let text = r.parentNode.parentNode.childNodes[1].innerHTML;

  // console.log(r.parentNode.parentNode.childNodes[1].innerHTML);
  let result = prompt("Please enter a task:", text);
  if (result == null || result == "") {
    txt = "User cancelled the prompt.";
  } else {
    //update via API

    let dataToPost = {
      name: result,
    };
    // console.log(dataToPost)
    fetch("http://localhost:8089/task/update/" + id, {
      method: "put",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(dataToPost),
    })
      // .then(res => res.json())
      .then(function (data) {
        console.log("Request succeeded with JSON response", data);
        readAll();
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });
  }
}

function taskList(r) {
  let id = r.parentNode.parentNode.childNodes[0].innerHTML;
  window.open("file:///C:/Users/saifr/Desktop/HTML/To-Do%20List/html/taskList.html?task_id="+id);
}

function readAll() {
  fetch("http://localhost:8089/task/read") //1
  
    .then(function (response) {
      if (response.status !== 200) {
        console.log(
          "Looks like there was a problem. Status Code: " + response.status
        );
        return;
      }
      // Examine the text in the response
      response.json().then(function (data) {
        console.log("here is my data", data);

        let table = document.getElementById("myTable");
        table.innerHTML = "";
        let tableHead = table.createTHead();
        let row = tableHead.insertRow();

        let th = document.createElement("th");
        let text = document.createTextNode("ID");
        th.appendChild(text);
        row.appendChild(th);

        th = document.createElement("th");
        text = document.createTextNode("Task");
        th.appendChild(text);
        row.appendChild(th);

        th = document.createElement("th");
        text = document.createTextNode("");
        th.appendChild(text);
        row.appendChild(th);

        th = document.createElement("th");
        text = document.createTextNode("");
        th.appendChild(text);
        row.appendChild(th);

        for (let element of data) {
          console.log(element);
          let row = table.insertRow(1);
          let cell1 = row.insertCell(0);
          let cell2 = row.insertCell(1);
          let cell3 = row.insertCell(2);
          let cell4 = row.insertCell(3);
          let cell5 = row.insertCell(4);

          cell1.innerHTML = element.id;
          cell2.innerHTML = element.name;
          cell3.innerHTML =
            "<button class='btn' id='updatebutton'onclick='updateRow(this)'><i class='fa fa-edit'></i></button>";
          cell4.innerHTML =
            "<button class='btn' id='deletebutton'onclick='deleteRow(this)'><i class='fa fa-trash'></i></button>";
          cell5.innerHTML =
            "<button class='btn' id='tasklistbutton' onclick='taskList(this)'><i class='fa fa-eye'></i></button>";
        }
      });
    })

    .catch(function (err) {
      console.log("Fetch Error :-S", err);
    });
}
