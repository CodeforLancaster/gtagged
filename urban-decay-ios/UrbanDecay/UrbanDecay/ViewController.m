//
//  ViewController.m
//  UrbanDecay
//
//  Created by Neil Morton on 27/11/2018.
//  Copyright © 2018 Code for Lancaster. All rights reserved.
//

#import "ViewController.h"

@interface ViewController () <UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@property (weak, nonatomic) IBOutlet UIButton *cameraButton;
@property (weak, nonatomic) IBOutlet UIView *scoreView;
@property (weak, nonatomic) IBOutlet UILabel *scoreLabel;
@property (nonatomic) bool cameraActive;


- (IBAction)cameraButtonPress:(id)sender;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    [self configureStartupValues];
}

-(void)configureStartupValues{
    _cameraActive = false;
    [_scoreLabel setText:@"0"];
    [_cameraButton setTitle:@"Snap" forState:UIControlStateNormal];
    
    // Make the cameraButton round
    CALayer *cameraButtonLayer = [_cameraButton layer];
    [cameraButtonLayer setMasksToBounds:YES];
    [cameraButtonLayer setCornerRadius:35.0f];
    
    // Round the score view corners
    CALayer *scoreViewLayer = [_scoreView layer];
    [scoreViewLayer setMasksToBounds:YES];
    [scoreViewLayer setCornerRadius:25.0f];
    
}

- (IBAction)cameraButtonPress:(id)sender {
    [self takePhoto];
}

-(void)takePhoto{
    
    if ([UIImagePickerController isSourceTypeAvailable:
         UIImagePickerControllerSourceTypeCamera])
    {
        
        UIImagePickerController *picker = [[UIImagePickerController alloc] init];
        picker.delegate = self;
        picker.allowsEditing = NO;
        picker.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        [self presentViewController:picker animated:YES completion:NULL];
        
    }
}

-(void)image:(UIImage *)image
finishedSavingWithError:(NSError *)error
 contextInfo:(void *)contextInfo
{
    if (error) {
        UIAlertController *alertController = [UIAlertController
                                              alertControllerWithTitle:@"Save failed"
                                              message:@"Failed to save image"
                                              preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *okAction = [UIAlertAction
                                   actionWithTitle:@"OK"
                                   style:UIAlertActionStyleDefault
                                   handler:^(UIAlertAction *action)
                                   {
                                       //
                                   }];
        [alertController addAction:okAction];
        [self presentViewController:alertController animated:YES completion:nil];
        
    }
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [self dismissViewControllerAnimated:YES completion:nil];
}


@end
