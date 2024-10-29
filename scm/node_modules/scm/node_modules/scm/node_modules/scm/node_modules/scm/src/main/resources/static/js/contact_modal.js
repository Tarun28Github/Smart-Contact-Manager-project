console.log ("contact_modal.js");
// const baseUrl="http://localhost:8080";
const baseUrl="http://scm01.ap-south-1.elasticbeanstalk.com";
const viewContactModal = document.getElementById("view_contact_modal");


// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options,instanceOptions);

function openContactModal(){
    contactModal.show();
}
function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
 console.log(id);

try {
    const data= await (
        await fetch(`${baseUrl}/scm/api/contact/${id}`)
    ).json();
    openContactModal();
    console.log(data);

    document.querySelector("#contact_name").innerHTML= data.name;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_address").innerHTML = data.address;
    document.querySelector("#contact_phone").innerHTML = data.phonenumber;
    document.querySelector("#contact_about").innerHTML = data.description;
    const contactFavorite = document.querySelector("#contact_favorite");
    if (data.favorite) {
      contactFavorite.innerHTML =
        "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
    } else {
      contactFavorite.innerHTML = "Not Favorite Contact";
    }

    document.querySelector("#contact_website").href = data.websitelink;
    document.querySelector("#contact_website").innerHTML = data.websitelink;
    document.querySelector("#contact_linkedIn").href = data.linkedlink;
    document.querySelector("#contact_linkedIn").innerHTML = data.linkedlink;
} catch (error) {
    console.log("Error ",error);
}
}

// delete contact function

async function deleteContactData(id){
    console.log(id)
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        
        if (result.isConfirmed) {
          Swal.fire({
            title: "Deleted!",
            text: "Your file has been deleted.",
            icon: "success"
          });
          const url= `${baseUrl}/scm/user/contacts/delete/`+id;
          window.location.replace(url);
        }
      });
 } 
