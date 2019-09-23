//
//  loginViewController.swift
//  UTAParkingApp
//
//  Created by Benjamin Anoh on 9/19/19.
//  Copyright © 2019 Benjamin Anoh. All rights reserved.
//

import UIKit
import Firebase

class loginViewController: UIViewController, UITextFieldDelegate {

    //Link textfields to variables
    
    @IBOutlet var emailTextfield: UITextField!
    @IBOutlet var passwordTextfield: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navBar()
        self.emailTextfield.delegate = self
        self.passwordTextfield.delegate = self
        

    }
    
    
    func navBar() {
        navigationController?.navigationBar.prefersLargeTitles = false
         navigationController?.isNavigationBarHidden = true
    }
    
    
    @IBAction func loginPressed(_ sender: AnyObject){
        
        Auth.auth().signIn(withEmail: emailTextfield.text!, password: passwordTextfield.text!) { (user, error) in
            
            
            //The completion will return the user info or error in caase of error
            //Display the error message to the user
            if error != nil {
                
                let alert = UIAlertController(title: "Incorrect", message: "\(error!.localizedDescription)", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok", style: .default, handler: nil))
                self.present(alert, animated: true)
                
                print(error!)
            }
            else {
                //perform segue will take the user to the parking locations screen
                 self.performSegue(withIdentifier: "goToParkingLocations", sender: self)
            }
        }
        
    }
    
    
    //Take user to registration screen
    @IBAction func registrationPressed(){
        performSegue(withIdentifier: "goToRegistration", sender: self)
    }
    
    
    //Return button closed when 
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return false
    }
}
