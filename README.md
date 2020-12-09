# CS4780Project2

The Cryptography project involves making use of the RSA Encryption algorithm. 3 Files are within the project that are essential to completing the algorithm.

1) The RSAGenKey allows a public and private key to be generated to encrypt and decrpt messages.
2) The RSAEncrypt takes in a plaintext and public key to encrypt the plaintext and send it back to the receipent
3) The RSADecrypt takes in an encrypted plaintext and uses the private key to decrypt the message from the sender


## Running the Program
Through the terminal (or wherever you can insert arguments) run the following commands 
1) Run "java RSAGenKey <lengthOfKey>" or "java RSAGenKey <p> <q> <e>" to generate the public and private keys
2) Run "java RSAEncrypt <plaintextFile> <publicKeyFile>" to encrypt the plaintextFile
3) Run "java RSADecrpyt <encryptedFile> <privateKeyFile>" to decrypt the message
