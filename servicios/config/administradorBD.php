<?php
include_once('config.php');
class administradorBD{
	public function executeQuery($sql){
		$conecta = mysql_connect(config::obtieneServidorBD(),config::obtieneUsuarioBD(),config::obtienePasswordBD());
		if(!$conecta){
			die('No puedo conectarme:' .mysql_error());
		}
		mysql_select_db(config::obtieneNombreBD(),$conecta);
		$result = mysql_query($sql);
		mysql_close($conecta);
		return $result;
	}
}
?>
