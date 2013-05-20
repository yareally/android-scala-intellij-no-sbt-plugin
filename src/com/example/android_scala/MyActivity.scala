/*
Copyright (c) $today.year Wes Lanning, http://codingcreation.com

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/
package com.example.android_scala

import android.app.Activity
import android.os.Bundle
import android.util.Log
import scala.util.continuations._

class MyActivity extends Activity
{
    /**
     * Test random scala stuff and see if it works on Android
     */
    override def onCreate(savedInstanceState: Bundle)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val a = new Complex(4.0, 5.0)
        val b = new Complex(2.0, 3.0)
        Log.d("MyActivity", "Printing scala test stuff")
        Log.d("MyActivity", (a + b).toString)
        Log.d("MyActivity", (a - b).toString)
        Log.d("MyActivity", a.toString)
        Log.d("MyActivity", b.toString)
        Log.d("MyActivity", baz().toString)
        Log.d("MyActivity", test().toString)
        Log.d("MyActivity", fun())
    }


    def fun() =
    {
        Log.d("MyActivity", "Not part of reset and so not part of the reified function.")
        reset {
            shift {
                // yes, the ⇒ actually works.
                k: (Int ⇒ Int) ⇒ // Reify reset body into Int => Int function
                    Log.d("MyActivity", "Calling reified function with value 2")
                    k(2)
            } * 300 // reset body _ * 300
        }

        Log.d("MyActivity", "Outside of reset. Thus not part of the delimited continuation")

        "Delimited continuations are ridiculous" // return value of fun
    }

    def test(): Int =
    {
        reset {
            shift {
                k: (Int ⇒ Int) ⇒
                    k(k(k(7)))
            } + 1
        } * 2
    }

    def foo() =
    {
        println("Once here.")
        shift((k: Int ⇒ Int) ⇒ k(k(k(7))))
    }

    def bar() =
    {
        1 + foo()
    }

    def baz() =
    {
        reset(bar() * 2)
    }


    class Complex(val real: Double, val imag: Double)
    {

        def +(that: Complex) =
            new Complex(this.real + that.real, this.imag + that.imag)

        def -(that: Complex) =
            new Complex(this.real - that.real, this.imag - that.imag)

        override def toString = real + " + " + imag + "i"

    }


}