<?php 

//connexion à la base de données
$host = "localhost";
$user = "technicien";
$password = "technicien";
$nom_db = "cashcash";
$conn = mysqli_connect($host, $user, $password, $nom_db);

if($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

    //getting the name from request 
    $numero_serie = $_POST['numero_serie'];
    $numero_intervention = $_POST['numero_intervention'];
    $temps_passer = $_POST['temps_passer'];
    $commentaire = $_POST['commentaire'];
 
    
    $req = "SELECT * FROM controler WHERE numero_intervention = ".$numero_intervention." AND numero_serie = ".$numero_serie;
    $donnees = mysqli_query($conn, $req);
    $row_cnt = mysqli_num_rows($donnees);
    
    if ($row_cnt == 0)
    {
        //creating a statement to insert to database 
        $stmt = $conn->prepare("INSERT INTO controler (numero_serie, numero_intervention, temps_passer, commentaire) values (?,?,?,?)");
    
 
        $stmt->bind_param("ssss", $numero_serie, $numero_intervention, $temps_passer, $commentaire);
        if($stmt->execute()){
        //making success response 
        $response['error'] = false; 
        $response['message'] = 'Control saved successfully'; 
        $requpdate = "UPDATE intervention SET statut = 1 WHERE numero_intervention = ".$numero_intervention;
        mysqli_query($conn, $requpdate);
    }
    else{
        //if not making failure response 
        $response['error'] = true; 
        $response['message'] = 'Please try later';
        }
    }
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid request"; 
}
 
//displaying the data in json format 
echo json_encode($response);

mysqli_close($conn);

?>
