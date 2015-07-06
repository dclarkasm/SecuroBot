package com.unhcfreg.securobot;

import java.util.Random;

/**
 * Created by Devon on 7/6/2015.
 */
public class TipEngine {
    Random r = new Random();
    private static final String tips[] = {
            "Keep your computer safe by choosing a password that includes letters," +
            " numbers, and special characters. Try not to include birthdays, common names, or places." +
            " Using obscure words mixed with an assortment of random numbers or symbols can better protect you" +
            "against brute force password hacking attacks and will keep your personal information safe.",

            //from http://www.dhs.gov/cybersecurity-tips
            "Keep your operating system, browser, anti-virus and other critical software up to date. " +
            "Security updates and patches are available for free from major companies.",

            "Verify the authenticity of requests from companies or individuals by contacting them directly." +
            "If you are being asked to provide personal information via email, you can independently contact " +
            "the company directly to verify this request.",

            "Pay close attention to website URLs. Pay attention to the URLs of websites you visit." +
            "Malicious websites sometimes use a variation in common spelling or a different domain " +
            "to deceive unsuspecting computer users. For example, they may use .com instead of .net",

            //from http://www.protectmyid.com/cyber-security
            "Before submitting credit card information online, look at the URL to ensure you're on a HTTPS" +
            "(Hypertext Transfer Protocol Secure) site. Be wary if a site requires information that isn't " +
            "necessary for a transaction. Information security is more important than anything you could buy.",

            "With the proper software installed, stolen laptops can be tracked to a physical location if they " +
            "are connected to the Internet. Other software gives you remote access for computer security with the " +
            "ability to erase your files or send them to a secure data center for recovery via the Web.",

            "You may receive a counterfeit text message that appears to be from a legitimate bank or credit card " +
            "company asking you verify your account information. Once you supply your information via phone or Web, " +
            "it will be in the hands of criminals. Be aware of information security by knowing when to ignore a text message.",

            //from http://www.ncpc.org/resources/files/pdf/fraud/10-tips-to-secure-brochure-pdf.pdf
            "Firewalls create a protective wall between your computer and the outside world. They come in two forms, " +
            "software firewalls that run on your personal computer and hardware firewalls that protect " +
            "a number of computers at the same time. Firewalls also ensure that unauthorized " +
            "persons can’t gain access to your computer while you’re connected to the Internet. ",

            "Disconnect your computer from the Internet when not in use. Disconnecting from the " +
            "Internet when you’re not online lessens the chance that someone will be able to access " +
            "your computer. And if you haven’t kept your anti-virus software up-to-date, or don’t have " +
            "a firewall in place, someone could infect your computer or use it to harm someone else " +
            "on the Internet."
    };

    public String generateGreeting() {
        int rn = r.nextInt(tips.length-0);
        return "Here's a tip. " + tips[rn];
    }
}
