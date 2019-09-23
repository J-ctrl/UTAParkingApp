//
//  registrationViewController.swift
//  UTAParkingApp
//
//  Created by Benjamin Anoh on 9/19/19.
//  Copyright © 2019 Benjamin Anoh. All rights reserved.
//

import UIKit
import Firebase
class registrationViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var firstName: UITextField!
    @IBOutlet weak var lastName: UITextField!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var reTypePassword: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        
        self.firstName.delegate = self
        self.lastName.delegate = self
        self.email.delegate = self
        self.password.delegate = self
        self.reTypePassword.delegate = self
    }
    
    
    
    @IBAction func registrationPressed(_ sender: Any) {
        
        
        //Check if the passwords match
        //If not display an alert, else procede
        if password.text != reTypePassword.text {
            let alert = UIAlertController(title: "Incorrect password", message: "The password do not match. Please check and try again", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "ok", style: .default, handler: nil))
            self.present(alert, animated: true)
        }
        else {
            
            Auth.auth().createUser(withEmail: email.text!, password: password.text!) { (user, error) in
                if error != nil {
                    
                    let alert = UIAlertController(title: "Incorrect", message: "\(error!.localizedDescription)", preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "ok", style: .default, handler: nil))
                    self.present(alert, animated: true)
                    
                    print(error!)
                }
                else {
                    self.performSegue(withIdentifier: "goToParkingLocations", sender: self)
                }
            }
            
        }
        
    }
    
    
    //Return button closed when
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return false
    }
    
}
