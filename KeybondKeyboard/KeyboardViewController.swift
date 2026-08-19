import UIKit
import SwiftUI

final class KeyboardViewController: UIInputViewController, KeyboardHost {

    private let model = KeyboardModel()
    private var hostingController: UIHostingController<KeyboardRootView>?
    private var heightConstraint: NSLayoutConstraint?

    override func viewDidLoad() {
        super.viewDidLoad()
        model.host = self
        installHostingController()
    }

    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        updateHeightConstraint()
    }

    override func textDidChange(_ textInput: UITextInput?) {
        super.textDidChange(textInput)
        // Keep the return key label / auto-capitalization in sync with the
        // host app's text field configuration.
        model.objectWillChange.send()
    }

    // MARK: - Setup

    private func installHostingController() {
        let root = KeyboardRootView(model: model)
        let hosting = UIHostingController(rootView: root)
        hostingController = hosting

        addChild(hosting)
        hosting.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(hosting.view)
        NSLayoutConstraint.activate([
            hosting.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            hosting.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            hosting.view.topAnchor.constraint(equalTo: view.topAnchor),
            hosting.view.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        hosting.didMove(toParent: self)

        let constraint = view.heightAnchor.constraint(equalToConstant: preferredKeyboardHeight())
        constraint.priority = .required
        constraint.isActive = true
        heightConstraint = constraint
    }

    private func updateHeightConstraint() {
        heightConstraint?.constant = preferredKeyboardHeight()
    }

    private func preferredKeyboardHeight() -> CGFloat {
        let isPad = UIDevice.current.userInterfaceIdiom == .pad
        let isLandscape = view.window?.windowScene?.interfaceOrientation.isLandscape ?? false
        if isPad {
            return isLandscape ? 420 : 320
        }
        return isLandscape ? 200 : 280
    }

    // MARK: - KeyboardHost

    func advanceToNextInputModeIfPossible() {
        advanceToNextInputMode()
    }

    func dismissKeyboardIfPossible() {
        dismissKeyboard()
    }
}
