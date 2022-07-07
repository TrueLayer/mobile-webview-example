//
//  ViewController.swift
//  WebViewExample
//
//  Created by Sébastien Kovacs on 06/07/2022.
//

import UIKit
import SafariServices

class ViewController: UIViewController {
  
  @IBOutlet weak var linkInput: UITextView!

  @IBOutlet weak var openInSafariVCBtn: UIButton!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    // Default links to our test page to trigger various bank links
    linkInput.text = "https://payment.truelayer-sandbox.com/test-redirect"
    linkInput.layer.cornerRadius = 5
    linkInput.layer.borderColor = UIColor.systemBlue.cgColor
    linkInput.layer.borderWidth = 1
    
    openInSafariVCBtn.titleLabel?.textAlignment = .center
    openInSafariVCBtn.titleLabel?.numberOfLines = 0
  }

  @IBAction func openInSafariVCTouched(_ sender: Any) {
    guard let url = getURL() else {
      return
    }
    
    let safariVC = SFSafariViewController(url: url)
      present(safariVC, animated: true, completion: nil)
  }
  
  private func getURL() -> URL? {
    guard let url = URL(string: linkInput.text) else {
      self.showAlert()
      return nil
    }
    
    if !UIApplication.shared.canOpenURL(url) {
      self.showAlert()
      return nil
    }
    
    return url
  }
  
  private func showAlert() {
    let alert = UIAlertController(title: "Invalid URL", message: "Please enter a valid URL before opening the webview", preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: "Default action"), style: .default))
    self.present(alert, animated: true, completion: nil)
  }
  
}

