//
//  parkingLocationViewController.swift
//  UTAParkingApp
//
//  Created by Benjamin Anoh on 9/19/19.
//  Copyright © 2019 Benjamin Anoh. All rights reserved.
//

import UIKit

class parkingLocationViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navBar()
    }
    
    func navBar() {
        navigationController?.navigationBar.prefersLargeTitles = true
        let searchLocation = UISearchController(searchResultsController: nil)
        navigationItem.searchController = searchLocation
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        self.navigationController?.navigationItem.backBarButtonItem?.tintColor = .clear
        let reminder = UIBarButtonItem(image: .init(imageLiteralResourceName: "alarm"), style: .plain, target: self, action: #selector(goToReminder))
        let logout = UIBarButtonItem(image: .init(imageLiteralResourceName: "logout"), style: .plain, target: self, action: #selector(goToHome))
        logout.tintColor = .orange
        self.navigationItem.rightBarButtonItems = [logout,reminder]
    }
    
    
    
    @objc func goToHome()
    {
        performSegue(withIdentifier: "goToHome", sender: self)
    }
    
    
    
    @objc func goToReminder()
    {
        performSegue(withIdentifier: "goToReminder", sender: self)
    }
}
